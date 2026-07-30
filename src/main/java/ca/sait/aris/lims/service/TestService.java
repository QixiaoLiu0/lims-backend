package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.ParameterDAO;
import ca.sait.aris.lims.dao.ResultDao;
import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.req.ResultBatchSaveReqDTO;
import ca.sait.aris.lims.dto.req.TestSaveReqDTO;
import ca.sait.aris.lims.dto.resp.TestAssignedRespDTO;
import ca.sait.aris.lims.dto.resp.TestResultRespDTO;

import ca.sait.aris.lims.entity.Parameter;
import ca.sait.aris.lims.entity.Result;
import ca.sait.aris.lims.entity.Test;
import ca.sait.aris.lims.util.DBUtil;
import ca.sait.aris.lims.util.UserContext;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestService {

    private final TestDao testDao = new TestDao();
    private final ResultDao resultDao = new ResultDao();
    private final ParameterDAO parameterDao = new ParameterDAO();


    // API 6: Add a Test and dynamically calculate the Run Number, generating placeholders.
    public TestAssignedRespDTO appendTestToSample(String sampleId, TestSaveReqDTO req) throws Exception {

        Connection conn = DBUtil.getConnection();
        String currentUserId = UserContext.getUserId();
        Date now = new Date();
        Timestamp nowTs = new Timestamp(now.getTime());

        try {
            conn.setAutoCommit(false);

            Integer testTypeId = req.getTestTypeId();

            // 1. Dynamic run number: first run for this sample+testType is 0.
            int nextRunNumber = testDao.getMaxRunNumber(sampleId, testTypeId) + 1;

            Test test = new Test();
            test.setTestId(UUID.randomUUID().toString());
            test.setSampleId(sampleId);
            test.setTestTypeId(testTypeId);
            test.setCreatedAt(now);
            test.setRunNumber(nextRunNumber);
            test.setRetestReason(null);

            testDao.insertTest(test);

            // 2. Pre-populate Result placeholders, one per Parameter under this TestType.
            List<Parameter> parameters = parameterDao.selectParametersByTestTypeId(testTypeId);
            List<Object[]> placeholderRows = new ArrayList<>();
            for (Parameter parameter : parameters) {
                placeholderRows.add(new Object[]{
                        UUID.randomUUID().toString(), // result_id
                        test.getTestId(),             // test_id
                        parameter.getParameterId(),   // parameter_id
                        nowTs,                        // created_at
                        currentUserId,                // created_by_user_id
                        ""                            // qualifier (placeholder)
                });
            }
            resultDao.batchInsertPlaceholders(placeholderRows);

            conn.commit();
            return new TestAssignedRespDTO(test.getTestId(), test.getRunNumber());

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


            // 1. Delete Result placeholders
            resultDao.deleteResultsByTestId(testId);

            // 2.  Delete the Test itself
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