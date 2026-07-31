package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.SampleDao;
import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.resp.AiChatRespDTO;
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

public class AiService {

    private static final String AI_SERVICE_URL = "http://localhost:5001/chat";

    private final TestDao testDao = new TestDao();
    private final SampleDao sampleDao = new SampleDao();
    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public AiChatRespDTO processAiQuery(String userPrompt) throws Exception {
        // 1. Call the Python NLU microservice (translation only — no DB access on its side)
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
            return degrade("AI service is currently unavailable. Please try again shortly.");
        }

        if (response.statusCode() != 200) {
            return degrade("AI service returned an unexpected error.");
        }

        // 2. Parse the AI service's response
        JsonObject aiResponse;
        try {
            aiResponse = gson.fromJson(response.body(), JsonObject.class);
        } catch (Exception e) {
            return degrade("AI service returned an unreadable response.");
        }

        boolean valid = aiResponse.has("valid") && aiResponse.get("valid").getAsBoolean();
        if (!valid) {
            return degrade("I couldn't understand that request. Try rephrasing it.");
        }

        boolean toolIsNull = !aiResponse.has("tool") || aiResponse.get("tool").isJsonNull();
        if (toolIsNull) {
            return degrade("I don't have a way to answer that yet.");
        }

        String toolName = aiResponse.get("tool").getAsString();
        JsonObject params = aiResponse.getAsJsonObject("params");

        // 3. Dispatch to the correct existing Service/DAO based on tool name
        try {
            return dispatchTool(toolName, params);
        } finally {
            DBUtil.closeConnection();
        }
    }

    /**
     * Core Business Dispatch: acts as a distributor to existing Service/DAO layers.
     * Any field or query type not explicitly handled here degrades gracefully
     * rather than throwing — this is the boundary the team lead asked for.
     */
    private AiChatRespDTO dispatchTool(String toolName, JsonObject params) {
        AiChatRespDTO resp = new AiChatRespDTO();
        resp.setToolUsed(toolName);

        try {
            switch (toolName) {
                case "get_latest_tests": {
                    if (params == null || !params.has("limit")) {
                        return degrade("Missing required information for that request.");
                    }
                    int limit = params.get("limit").getAsInt();
                    List<Test> tests = testDao.selectLatestTests(limit);
                    resp.setResults(tests);
                    resp.setMessage("Found " + tests.size() + " recent test(s).");
                    return resp;
                }
                case "get_samples_by_status": {
                    if (params == null || !params.has("status")) {
                        return degrade("Missing required information for that request.");
                    }
                    String status = params.get("status").getAsString();
                    List<Sample> samples = sampleDao.selectSamplesByStatus(status);
                    resp.setResults(samples);
                    resp.setMessage("Found " + samples.size() + " sample(s) with status '" + status + "'.");
                    return resp;
                }
                default: {
                    // Query type not in our predefined, supported set — graceful degradation
                    return degrade("That type of question isn't supported yet.");
                }
            }
        } catch (Exception e) {
            System.err.println("[AiService] dispatchTool failed for tool '" + toolName + "': " + e.getMessage());
            e.printStackTrace();
            return degrade("Something went wrong retrieving that information.");
        }
    }

    private AiChatRespDTO degrade(String friendlyMessage) {
        AiChatRespDTO resp = new AiChatRespDTO();
        resp.setMessage(friendlyMessage);
        return resp;
    }
}