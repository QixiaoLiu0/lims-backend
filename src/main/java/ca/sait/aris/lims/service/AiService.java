package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.resp.AiChatRespDTO;
import ca.sait.aris.lims.entity.Test;
import ca.sait.aris.lims.util.DBUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class AiService {

    private static final String AI_SERVICE_URL = "http://localhost:5001/chat";

    private final TestDao testDao = new TestDao();
    private final Gson gson = new Gson();
   

    public AiChatRespDTO processAiQuery(String userPrompt) throws Exception {
        
        // 1. Call the Python NLU microservice (translation only - no DB access on its side)
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("prompt", userPrompt);
        String jsonInputString = gson.toJson(requestBody);

        String responseBodyString = null;
        int statusCode = 0;

        try {
            java.net.URL url = new java.net.URL(AI_SERVICE_URL);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.setConnectTimeout(5000); //  connectTimeout
            con.setReadTimeout(30000);   // timeout(Duration.ofSeconds(30))

            // Write to request body 
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            statusCode = con.getResponseCode();

            // if successful, read response body
            if (statusCode == 200) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
                responseBodyString = response.toString();
            }

        } catch (Exception e) {
            return degrade("AI service is currently unavailable. Please try again shortly.");
        }

        if (statusCode != 200) {
            return degrade("AI service returned an unexpected error.");
        }

        // 2. Parse the AI service's response
        JsonObject aiResponse;
        try {
            aiResponse = gson.fromJson(responseBodyString, JsonObject.class);
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
                            || !params.has("id")
                            || params.get("id").isJsonNull()) {
                        return degrade("Missing test ID.");
                    }

                    String testId = params.get("id").getAsString();
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
                            || !params.has("sample_id")
                            || params.get("sample_id").isJsonNull()) {
                        return degrade("Missing sample ID.");
                    }

                    String sampleId = params.get("sample_id").getAsString();
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