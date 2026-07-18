package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dto.req.ResultBatchSaveReqDTO;
import ca.sait.aris.lims.dto.req.TestSaveReqDTO;
import ca.sait.aris.lims.dto.resp.TestAssignedRespDTO;
import ca.sait.aris.lims.dto.resp.TestResultRespDTO;
import java.util.List;

public class TestService {

    // API 6: Add a Test and dynamically calculate the Run Number, generating placeholders.
    public TestAssignedRespDTO appendTestToSample(String sampleId, TestSaveReqDTO req) throws Exception {
    	//TODO
		return null;
        
    }

    // API 8: Cascade Physical Deletion of Test
    public void deleteTest(String testId) throws Exception {
    	//TODO
    }

    // API 9: Get Test Results (Placeholder Retrieval)
    public List<TestResultRespDTO> getTestResults(String testId) throws Exception {
    	//TODO
		return null;
        
    }

    // API 10: Batch saving of Results & Status Rollup
    public void saveTestResults(String testId, ResultBatchSaveReqDTO req) throws Exception {
    	//TODO
    }
}