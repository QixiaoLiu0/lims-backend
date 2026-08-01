package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.SampleDetailTestRespDTO;
import ca.sait.aris.lims.entity.Test;
import ca.sait.aris.lims.dao.BaseJdbcDao;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import ca.sait.aris.lims.util.DBUtil;

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
    	String sql = "DELETE FROM test WHERE test_id = ?";
        executeUpdate(sql, testId);
    }

    public Test selectTestById(String testId) throws Exception {
        String sql = "SELECT test_id, sample_id, test_type_id, status, " +
                "created_at, run_number, retest_reason " +
                "FROM test WHERE test_id = ?";

        return executeQueryForObject(sql, Test.class, testId);
    }

    // Query the Test list for the Sample details page.
    public List<SampleDetailTestRespDTO> selectTestsBySampleId(String sampleId) throws Exception {
        String sql = "SELECT test_id FROM test WHERE sample_id = ?";
        return executeQuery(sql, SampleDetailTestRespDTO.class, sampleId);
    }


    /**
     * Dynamic calculation of Run Number
     * @return -1 if no prior test exists for this sample+testType, so callers do (result + 1)
     * to get 0 for a first run, 1 for a first retest, etc.
     */
    public int getMaxRunNumber(String sampleId, Integer testTypeId) throws Exception {
        String sql = "SELECT COALESCE(MAX(run_number), -1) FROM test WHERE sample_id = ? AND test_type_id = ?";

        Connection conn = DBUtil.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sampleId);
            stmt.setInt(2, testTypeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
        
    }

    // status Bubble Update
    public void updateTestStatus(String testId, String status) throws Exception {
        String sql = "UPDATE test SET status = ? WHERE test_id = ?";
        executeUpdate(sql, status, testId);
    }

    // Check if there are any incomplete tests under a given Sample.
    public int countIncompleteTestsBySampleId(String sampleId) throws Exception {
        String sql = "SELECT COUNT(*) FROM test " +
                "WHERE sample_id = ? " +
                "AND (status IS NULL OR status <> 'Completed')";

        Integer count = executeQueryForScalar(sql, Integer.class, sampleId);
        return count == null ? 0 : count;
    }
}