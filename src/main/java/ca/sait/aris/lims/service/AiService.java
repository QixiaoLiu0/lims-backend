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
import java.util.Collections;

public class AiService {

    private static final String AI_SERVICE_URL = "http://localhost:5001/chat";

    private final TestDao testDao = new TestDao();
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
                case "find_tests_by_id": {
                    if (params == null
                            || !params.has("testId")
                            || params.get("testId").isJsonNull()) {
                        return degrade("Missing test ID.");
                    }

                    String testId = params.get("testId").getAsString();
                    Test test = testDao.selectTestById(testId);

                    if (test == null) {
                        resp.setResults(Collections.emptyList());
                        resp.setMessage("No test found with ID '" + testId + "'.");
                        return resp;
                    }

                    // AiChatRespDTO expects a List, even though this query finds one test.
                    resp.setResults(Collections.singletonList(test));
                    resp.setMessage("Found test '" + testId + "'.");
                    return resp;
                }

                case "count_incomplete_tests_by_sampleid": {
                    if (params == null
                            || !params.has("sampleId")
                            || params.get("sampleId").isJsonNull()) {
                        return degrade("Missing sample ID.");
                    }

                    String sampleId = params.get("sampleId").getAsString();
                    int incompleteCount =
                            testDao.countIncompleteTestsBySampleId(sampleId);

                    // Results is List<?>; wrap the scalar count in a one-item list.
                    resp.setResults(Collections.singletonList(incompleteCount));
                    resp.setMessage("Found " + incompleteCount
                            + " incomplete test(s) for sample '" + sampleId + "'.");
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