package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.CocDao;
import ca.sait.aris.lims.dao.ResultDao;
import ca.sait.aris.lims.dao.SampleDao;
import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.req.CocSaveReqDTO;
import ca.sait.aris.lims.dto.resp.*;
import ca.sait.aris.lims.util.DBUtil;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class CocService {

    private final CocDao cocDao = new CocDao();
    private final SampleDao sampleDao = new SampleDao();
    private final TestDao testDao = new TestDao();
    private final ResultDao resultDao = new ResultDao();

    // API 4: Multi-table, atomic aggregation creation (Hierarchical Aggregate Creation)
    public String createCoc(CocSaveReqDTO req) throws Exception {
        //TODO
        return null;
    }

    // API 3: Cascade Physical Deletion
    public void deleteCoc(String cocId) throws Exception {
        Connection conn = DBUtil.getConnection();

        try {
            conn.setAutoCommit(false);

            // 1. Find all Samples under this COC
            List<CocDetailSampleRespDTO> samples = sampleDao.selectSamplesByCocId(cocId);

            for (CocDetailSampleRespDTO sample : samples) {
                String sampleId = sample.getSampleId();

                // 2. Find all Tests under this Sample
                List<SampleDetailTestRespDTO> tests = testDao.selectTestsBySampleId(sampleId);

                for (SampleDetailTestRespDTO test : tests) {
                    String testId = test.getTestId();

                    // 3. Delete Results first (deepest level)
                    resultDao.deleteResultsByTestId(testId);

                    // 4. Then delete the Test
                    testDao.deleteTestById(testId);
                }

                // 5. Then delete the Sample
                sampleDao.deleteSampleById(sampleId);
            }

            // 6. Finally delete the COC itself
            cocDao.deleteCocById(cocId);

            conn.commit();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception rollbackEx) {
                System.err.println("[CocService] Rollback failed: " + rollbackEx.getMessage());
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

    // API 11: Avoids N+1 memory aggregation queries for the Dashboard
    public List<DashboardCocRespDTO> getDashboardCocs() throws Exception {
        //TODO
        return null;
    }

    // API 12: Get COC Details
    public CocDetailRespDTO getCocDetail(String cocId) throws Exception {
        //TODO
        return null;
    }

    // --- Helpers ---

    private Date toDate(LocalDateTime ldt) {
        if (ldt == null) return null;
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}