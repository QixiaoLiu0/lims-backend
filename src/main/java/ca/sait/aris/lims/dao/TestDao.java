package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.SampleDetailTestRespDTO;
import ca.sait.aris.lims.entity.Test;
import java.util.List;

public class TestDao extends BaseJdbcDao {

    // Do not insert the status field
    public void insertTest(Test test) throws Exception {
        String sql = "INSERT INTO test (test_id, sample_id, test_type_id, created_at, run_number, retest_reason) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        executeUpdate(sql,
                test.getTestId(),
                test.getSampleId(),
                test.getTestTypeId(),
                toTimestamp(test.getCreatedAt()),
                test.getRunNumber(),
                test.getRetestReason()
        );
    }

    public void deleteTestById(String testId) throws Exception {
    	//TODO
    }

    public Test selectTestById(String testId) throws Exception {
    	//TODO
		return null;
        
    }

    // Query the Test list for the Sample details page.
    public List<SampleDetailTestRespDTO> selectTestsBySampleId(String sampleId) throws Exception {
    	//TODO
		return null;
        
    }

    // Dynamic calculation of Run Number
    public int getMaxRunNumber(String sampleId, Integer testTypeId) throws Exception {
    	//TODO
		return 0;
        
    }

    // status Bubble Update
    public void updateTestStatus(String testId, String status) throws Exception {
    	//TODO
    }

    // Check if there are any incomplete tests under a given Sample.
    public int countIncompleteTestsBySampleId(String sampleId) throws Exception {
		return 0;
        
    }
}