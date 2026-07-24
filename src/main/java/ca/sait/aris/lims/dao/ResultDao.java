package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.TestResultRespDTO;
import java.util.List;

public class ResultDao extends BaseJdbcDao {

	/**
	 * [Rules]: When initializing place holders, only write created_by_user_id and created_at.
	 * updated_by_user_id and updated_at should remain NULL (the database default).
	 * @param paramsList Each Object[] contains: [result_id, test_id, parameter_id, created_at, created_by_user_id, qualifier]
	 */
    public void batchInsertPlaceholders(List<Object[]> paramsList) throws Exception {
        //TODO
    }

    /**
     * [Rules]: When saving test results in batches, UPDATE must be used, and both updated_by_user_id and updated_at must be updated.
     * @param paramsList Each Object[] contains: [value, qualifier, updated_by_user_id, updated_at, result_id]
     */
    public void batchUpdateResults(List<Object[]> paramsList) throws Exception {
    	//TODO
    }

    public void deleteResultsByTestId(String testId) throws Exception {
    	String sql = "DELETE FROM result WHERE test_id = ?";
        executeUpdate(sql, testId);
    }

    // Join query: Retrieves blueprint metadata for Result place holders and Parameters.
    public List<TestResultRespDTO> selectResultsByTestId(String testId) throws Exception {
        String sql = "SELECT r.result_id, r.parameter_id, p.parameter_name, p.unit, p.`limit`, r.value, r.qualifier " +
                "FROM result r " +
                "JOIN parameter p ON r.parameter_id = p.parameter_id " +
                "WHERE r.test_id = ? " +
                "ORDER BY p.parameter_id";
        return executeQuery(sql, TestResultRespDTO.class, testId);
    }
}