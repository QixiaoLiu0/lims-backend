package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.TestAssignedRespDTO;
import ca.sait.aris.lims.dto.resp.TestResultRespDTO;
import ca.sait.aris.lims.service.TestService;

import java.util.List;

//sprint 3
public class TestController {

    private final TestService testService = new TestService();

    // API 6: Append Test to Sample
    public RespResult<TestAssignedRespDTO> appendTestToSample(String sampleId, String jsonBody) {
        //TODO
        return null;
    }

    // API 8: Delete Test Task
    public RespResult<Object> deleteTest(String testId) {
        try {
            testService.deleteTest(testId);
            return RespResult.success();
        } catch (Exception e) {
            System.err.println("[TestController] deleteTest failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to delete test: " + testId);
        }
    }

    // API 9: Get Test Results
    public RespResult<List<TestResultRespDTO>> getTestResults(String testId) {
        try {
            List<TestResultRespDTO> results = testService.getTestResults(testId);
            return RespResult.success(results);
        } catch (Exception e) {
            System.err.println("[TestController] getTestResults failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to retrieve test results: " + testId);
        }
    }

    // API 10: Batch Save Test Results
    public RespResult<Object> saveTestResults(String testId, String jsonBody) {
        //TODO
        return null;
    }
}