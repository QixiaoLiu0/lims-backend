package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.ResultDao;
import ca.sait.aris.lims.dao.SampleDao;
import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.req.SampleSaveReqDTO;
import ca.sait.aris.lims.dto.resp.SampleDetailRespDTO;
import ca.sait.aris.lims.dto.resp.SampleDetailTestRespDTO;
import ca.sait.aris.lims.util.DBUtil;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class SampleService {

    private final SampleDao sampleDao = new SampleDao();
    private final TestDao testDao = new TestDao();
    private final ResultDao resultDao = new ResultDao();

    // API 5: Append Sample to coc
    public String appendSampleToCoc(String cocId, SampleSaveReqDTO req) throws Exception {
        //TODO
        return cocId;
    }

    // API 7: Cascade Physical delete Sample
    public void deleteSample(String sampleId) throws Exception {
        Connection conn = DBUtil.getConnection();

        try {
            conn.setAutoCommit(false);

            // 1. Find all Tests under this Sample
            List<SampleDetailTestRespDTO> tests = testDao.selectTestsBySampleId(sampleId);

            for (SampleDetailTestRespDTO test : tests) {
                String testId = test.getTestId();

                // 2. Delete Results first (deepest level)
                resultDao.deleteResultsByTestId(testId);

                // 3. Then delete the Test
                testDao.deleteTestById(testId);
            }

            // 4. Finally delete the Sample itself
            sampleDao.deleteSampleById(sampleId);

            conn.commit();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception rollbackEx) {
                System.err.println("[SampleService] Rollback failed: " + rollbackEx.getMessage());
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

    // API 13: Get Sample Details
    public SampleDetailRespDTO getSampleDetail(String sampleId) throws Exception {
        //TODO
        return null;
    }

    //helpers
    /**
     * 1. For write operations: The LocalDateTime passed to the DTO is converted into the java.util.Date object required by the persistent layer entity.
     */
    private Date toDate(LocalDateTime ldt) {
        if (ldt == null) return null;
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 2. Used for read operations: Retrieving a java.util.Date from a database/Entity and converting it to the required LocalDateTime for a DTO.
     */
    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}