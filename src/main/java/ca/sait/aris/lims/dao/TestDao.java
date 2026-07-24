package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.SampleDetailTestRespDTO;
import ca.sait.aris.lims.entity.Test;
import java.util.List;

public class TestDao extends BaseJdbcDao {

    // Do not insert the status field
    public void insertTest(Test test) throws Exception {
        //TODO
    }

    public void deleteTestById(String testId) throws Exception {
    	String sql = "DELETE FROM test WHERE test_id = ?";
        executeUpdate(sql, testId);
    }

    public Test selectTestById(String testId) throws Exception {
    	//TODO
		return null;
        
    }

    // Query the Test list for the Sample details page.
    public List<SampleDetailTestRespDTO> selectTestsBySampleId(String sampleId) throws Exception {
        String sql = "SELECT test_id FROM test WHERE sample_id = ?";
        return executeQuery(sql, SampleDetailTestRespDTO.class, sampleId);
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