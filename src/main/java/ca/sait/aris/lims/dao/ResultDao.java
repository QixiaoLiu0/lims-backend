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
        if (paramsList == null || paramsList.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO result (result_id, test_id, parameter_id, created_at, created_by_user_id, qualifier) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        executeBatchUpdate(sql, paramsList);
    }

    /**
     * [Rules]: When saving test results in batches, UPDATE must be used, and both updated_by_user_id and updated_at must be updated.
     * @param paramsList Each Object[] contains: [value, qualifier, updated_by_user_id, updated_at, result_id]
     */
    public void batchUpdateResults(List<Object[]> paramsList) throws Exception {
    	//TODO
    }

    public void deleteResultsByTestId(String testId) throws Exception {
    	//TODO
    }

    // Join query: Retrieves blueprint metadata for Result place holders and Parameters.
    public List<TestResultRespDTO> selectResultsByTestId(String testId) throws Exception {
    	//TODO
		return null;
    }
}