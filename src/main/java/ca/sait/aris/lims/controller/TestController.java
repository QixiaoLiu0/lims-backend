package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.TestAssignedRespDTO;
import ca.sait.aris.lims.dto.resp.TestResultRespDTO;

import java.util.List;

//sprint 3
public class TestController {

    // API 6: Append Test to Sample
    public RespResult<TestAssignedRespDTO> appendTestToSample(String sampleId, String jsonBody) {
    	//TODO
		return null;
        
    }

    // API 8: Delete Test Task
    public RespResult<Object> deleteTest(String testId) {
    	//TODO
		return null;
        
    }

    // API 9: Get Test Results
    public RespResult<List<TestResultRespDTO>> getTestResults(String testId) {
    	//TODO
		return null;
        
    }

    // API 10: Batch Save Test Results
    public RespResult<Object> saveTestResults(String testId, String jsonBody) {
    	//TODO
		return null;
        
    }
}