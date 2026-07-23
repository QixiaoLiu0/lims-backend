package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.ResultDao;
import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.req.ResultBatchSaveReqDTO;
import ca.sait.aris.lims.dto.req.TestSaveReqDTO;
import ca.sait.aris.lims.dto.resp.TestAssignedRespDTO;
import ca.sait.aris.lims.dto.resp.TestResultRespDTO;
import ca.sait.aris.lims.util.DBUtil;

import java.sql.Connection;
import java.util.List;

public class TestService {

    private final TestDao testDao = new TestDao();
    private final ResultDao resultDao = new ResultDao();

    // API 6: Add a Test and dynamically calculate the Run Number, generating placeholders.
    public TestAssignedRespDTO appendTestToSample(String sampleId, TestSaveReqDTO req) throws Exception {
        //TODO
        return null;
    }

    // API 8: Cascade Physical Deletion of Test
    public void deleteTest(String testId) throws Exception {
        Connection conn = DBUtil.getConnection();

        try {
            conn.setAutoCommit(false);

            // 1. Delete Result placeholders first (deepest level)
            resultDao.deleteResultsByTestId(testId);

            // 2. Then delete the Test itself
            testDao.deleteTestById(testId);

            conn.commit();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception rollbackEx) {
                System.err.println("[TestService] Rollback failed: " + rollbackEx.getMessage());
            }
            throw e;

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception ignored) {
            }
            DBUtil.closeConnection();
        }
    }

    // API 9: Get Test Results (Placeholder Retrieval)
    public List<TestResultRespDTO> getTestResults(String testId) throws Exception {
        try {
            return resultDao.selectResultsByTestId(testId);
        } finally {
            DBUtil.closeConnection();
        }
    }

    // API 10: Batch saving of Results & Status Rollup
    public void saveTestResults(String testId, ResultBatchSaveReqDTO req) throws Exception {
        //TODO
    }
}