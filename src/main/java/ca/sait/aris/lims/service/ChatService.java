package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.SampleDao;
import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.resp.ChatQueryRespDTO;
import ca.sait.aris.lims.entity.Sample;
import ca.sait.aris.lims.entity.Test;
import ca.sait.aris.lims.util.DBUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ChatService {

    private static final String AI_SERVICE_URL = "http://localhost:5001/chat";

    private final TestDao testDao = new TestDao();
    private final SampleDao sampleDao = new SampleDao();
    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public ChatQueryRespDTO processChat(String userPrompt) throws Exception {
        // 1. Call the Python microservice
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("prompt", userPrompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AI_SERVICE_URL))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            ChatQueryRespDTO errorResp = new ChatQueryRespDTO();
            errorResp.setMessage("AI service is unavailable. Is it running on port 5001?");
            return errorResp;
        }

        if (response.statusCode() != 200) {
            ChatQueryRespDTO errorResp = new ChatQueryRespDTO();
            errorResp.setMessage("AI service returned an error (HTTP " + response.statusCode() + ").");
            return errorResp;
        }

        // 2. Parse the AI service's response
        JsonObject aiResponse = gson.fromJson(response.body(), JsonObject.class);
        boolean valid = aiResponse.has("valid") && aiResponse.get("valid").getAsBoolean();

        if (!valid) {
            ChatQueryRespDTO errorResp = new ChatQueryRespDTO();
            String errorMsg = aiResponse.has("error") && !aiResponse.get("error").isJsonNull()
                    ? aiResponse.get("error").getAsString()
                    : "I couldn't understand that request.";
            errorResp.setMessage(errorMsg);
            return errorResp;
        }

        boolean toolIsNull = !aiResponse.has("tool") || aiResponse.get("tool").isJsonNull();
        if (toolIsNull) {
            ChatQueryRespDTO noMatchResp = new ChatQueryRespDTO();
            noMatchResp.setMessage("I don't have a way to answer that yet.");
            return noMatchResp;
        }

        String toolName = aiResponse.get("tool").getAsString();
        JsonObject params = aiResponse.getAsJsonObject("params");

        // 3. Dispatch to the correct DAO based on tool name
        try {
            return dispatchTool(toolName, params);
        } finally {
            DBUtil.closeConnection();
        }
    }

    private ChatQueryRespDTO dispatchTool(String toolName, JsonObject params) throws Exception {
        ChatQueryRespDTO resp = new ChatQueryRespDTO();
        resp.setToolUsed(toolName);

        switch (toolName) {
            case "get_latest_tests": {
                int limit = params.get("limit").getAsInt();
                List<Test> tests = testDao.selectLatestTests(limit);
                resp.setResults(tests);
                resp.setMessage("Found " + tests.size() + " recent test(s).");
                return resp;
            }
            case "get_samples_by_status": {
                String status = params.get("status").getAsString();
                List<Sample> samples = sampleDao.selectSamplesByStatus(status);
                resp.setResults(samples);
                resp.setMessage("Found " + samples.size() + " sample(s) with status '" + status + "'.");
                return resp;
            }
            default: {
                resp.setToolUsed(null);
                resp.setMessage("Unknown tool requested: " + toolName);
                return resp;
            }
        }
    }
}