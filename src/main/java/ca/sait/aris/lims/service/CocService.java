package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.CocDao;
import ca.sait.aris.lims.dao.ParameterDAO;
import ca.sait.aris.lims.dao.ResultDao;
import ca.sait.aris.lims.dao.SampleDao;
import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.req.CocSaveReqDTO;
import ca.sait.aris.lims.dto.req.SampleSaveReqDTO;
import ca.sait.aris.lims.entity.Coc;
import ca.sait.aris.lims.entity.Parameter;
import ca.sait.aris.lims.entity.Sample;
import ca.sait.aris.lims.entity.Test;
import ca.sait.aris.lims.dto.resp.*;
import ca.sait.aris.lims.entity.Coc;

import ca.sait.aris.lims.entity.Sample;

import ca.sait.aris.lims.util.DBUtil;
import ca.sait.aris.lims.util.UserContext;


import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class CocService {

    private final CocDao cocDao = new CocDao();
    private final SampleDao sampleDao = new SampleDao();
    private final TestDao testDao = new TestDao();
    private final ResultDao resultDao = new ResultDao();
    private final ParameterDAO parameterDao = new ParameterDAO();


    // API 4: Multi-table, atomic aggregation creation (Hierarchical Aggregate Creation)
    /**
     * Creates a full COC hierarchy (COC -> Samples -> Tests -> Result placeholders)
     * in a single atomic transaction. Supports empty, semi-populated, and fully
     * populated nested arrays.
     */

    // ATTENTION!!!ACHTUNG!!! qualifier has placeholder for now. not sure what's what

    public String createCoc(CocSaveReqDTO req) throws Exception {

        Connection conn = DBUtil.getConnection();
        String currentUserId = UserContext.getUserId();
        Date now = new Date();
        Timestamp nowTs = new Timestamp(now.getTime());

        try {
            conn.setAutoCommit(false);

            // 1. Build & insert COC (status omitted -> DB default 'In-Progress')
            Coc coc = new Coc();
            coc.setCocId(UUID.randomUUID().toString());
            coc.setCocNumber(req.getCocNumber());
            coc.setProjectName(req.getProjectName());
            coc.setReportToName1(req.getReportToName1());
            coc.setReportToEmail1(req.getReportToEmail1());
            coc.setReportToName2(req.getReportToName2());
            coc.setReportToEmail2(req.getReportToEmail2());
            coc.setDateRequired(toDate(req.getDateRequired()));
            coc.setIsRush(req.getIsRush());
            coc.setDateForRush(toDate(req.getDateForRush()));
            coc.setReceivedBy(req.getReceivedBy());
            coc.setReceivedTime(toDate(req.getReceivedTime()));
            coc.setRelinquishedBy(req.getRelinquishedBy());
            coc.setRelinquishedTime(toDate(req.getRelinquishedTime()));
            coc.setNumberOfContainers(req.getNumberOfContainers());
            coc.setSpecialInstructions(req.getSpecialInstructions());
            coc.setCreatedByUserId(currentUserId);
            coc.setCreatedAt(now);

            cocDao.insertCoc(coc);

            // Collects placeholder rows across every Sample/Test in this COC,
            // so we can batch-insert them all in one call at the end.
            List<Object[]> placeholderRows = new ArrayList<>();

            // 2. Supports empty COC: samples list may legitimately be null/empty.
            if (req.getSamples() != null) {
                for (SampleSaveReqDTO sampleDto : req.getSamples()) {

                    Sample sample = new Sample();
                    sample.setSampleId(UUID.randomUUID().toString());
                    sample.setCocId(coc.getCocId());
                    sample.setSampleTypeId(sampleDto.getSampleTypeId());
                    sample.setSampleClientId(sampleDto.getSampleClientId());
                    sample.setSampledTime(toDate(sampleDto.getSampledTime()));
                    sample.setSamplingPoint(sampleDto.getSamplingPoint());
                    sample.setMatrix(sampleDto.getMatrix());
                    sample.setNumberOfContainers(sampleDto.getNumberOfContainers());
                    sample.setRemarks(sampleDto.getRemarks());
                    sample.setInitialVolume(sampleDto.getInitialVolume());
                    sample.setRemainingVolume(sampleDto.getRemainingVolume());
                    sample.setCreatedAt(now);
                    sample.setIsFiltered(sampleDto.getIsFiltered());
                    sample.setIsPreserved(sampleDto.getIsPreserved());
                    sample.setIsFilteredAndPreserved(sampleDto.getIsFilteredAndPreserved());

                    sampleDao.insertSample(sample);

                    // 3. Supports semi-empty COC: testTypeIds may legitimately be null/empty
                    // (a sample can exist with no tests assigned yet).
                    if (sampleDto.getTestTypeIds() != null) {
                        for (Integer testTypeId : sampleDto.getTestTypeIds()) {

                            // First run for this sample+testType is 0 (-1 + 1).
                            int nextRunNumber = testDao.getMaxRunNumber(sample.getSampleId(), testTypeId) + 1;

                            Test test = new Test();
                            test.setTestId(UUID.randomUUID().toString());
                            test.setSampleId(sample.getSampleId());
                            test.setTestTypeId(testTypeId);
                            test.setCreatedAt(now);
                            test.setRunNumber(nextRunNumber);
                            test.setRetestReason(null);

                            testDao.insertTest(test);

                            // 4. Fully populated COC: pre-create a Result placeholder row
                            // for every Parameter under this Test's TestType, so the
                            // "enter results" screen has rows ready to fill in later.
                            List<Parameter> parameters = parameterDao.selectParametersByTestTypeId(testTypeId);
                            for (Parameter parameter : parameters) {
                                placeholderRows.add(new Object[]{
                                        UUID.randomUUID().toString(),  // result_id
                                        test.getTestId(),              // test_id
                                        parameter.getParameterId(),    // parameter_id
                                        nowTs,                         // created_at
                                        currentUserId,                 // created_by_user_id
                                        // ---------------------------------------------------------------------------------------------------------->
                                        ""                              // qualifier (placeholder)
                                });
                            }
                        }
                    }
                }
            }

            // 5. One batch insert for every Result placeholder across the whole COC.
            resultDao.batchInsertPlaceholders(placeholderRows);

            conn.commit();
            return coc.getCocId();

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
        try {
            // 1. One query for all COCs + their aggregated test counts.
            List<DashboardCocRespDTO> cocs = cocDao.selectDashboardCocs();
            if (cocs.isEmpty()) {
                return cocs;
            }

            // 2. One batched query for every Sample across all those COCs (N+1 defense).
            List<String> cocIds = new ArrayList<>();
            for (DashboardCocRespDTO coc : cocs) {
                cocIds.add(coc.getCocId());
            }
            List<Sample> samples = sampleDao.selectSamplesByCocIds(cocIds);

            // 3. Group samples by cocId in memory, then attach to each DTO.
            Map<String, List<DashboardSampleRespDTO>> samplesByCocId = new HashMap<>();
            for (Sample sample : samples) {
                DashboardSampleRespDTO sampleDto = new DashboardSampleRespDTO();
                sampleDto.setSampleId(sample.getSampleId());
                sampleDto.setSampleClientId(sample.getSampleClientId());
                sampleDto.setMatrix(sample.getMatrix());
                samplesByCocId
                        .computeIfAbsent(sample.getCocId(), k -> new ArrayList<>())
                        .add(sampleDto);
            }

            for (DashboardCocRespDTO coc : cocs) {
                coc.setSamples(samplesByCocId.getOrDefault(coc.getCocId(), new ArrayList<>()));
            }

            return cocs;

        } finally {
            DBUtil.closeConnection();
        }
    }

    // API 12: Get COC Details
    public CocDetailRespDTO getCocDetail(String cocId) throws Exception {
        try {
            Coc entity = cocDao.selectCocById(cocId);
            if (entity == null) {
                return null;
            }

            CocDetailRespDTO dto = new CocDetailRespDTO();
            dto.setCocId(entity.getCocId());
            dto.setCocNumber(entity.getCocNumber());
            dto.setProjectName(entity.getProjectName());
            dto.setReportToName1(entity.getReportToName1());
            dto.setReportToEmail1(entity.getReportToEmail1());
            dto.setReportToName2(entity.getReportToName2());
            dto.setReportToEmail2(entity.getReportToEmail2());
            dto.setDateRequired(toLocalDateTime(entity.getDateRequired()));
            dto.setIsRush(entity.getIsRush());
            dto.setDateForRush(toLocalDateTime(entity.getDateForRush()));
            dto.setReceivedBy(entity.getReceivedBy());
            dto.setReceivedTime(toLocalDateTime(entity.getReceivedTime()));
            dto.setRelinquishedBy(entity.getRelinquishedBy());
            dto.setRelinquishedTime(toLocalDateTime(entity.getRelinquishedTime()));
            dto.setNumberOfContainers(entity.getNumberOfContainers());
            dto.setSpecialInstructions(entity.getSpecialInstructions());
            dto.setCreatedByUserId(entity.getCreatedByUserId());
            dto.setCreatedAt(toLocalDateTime(entity.getCreatedAt()));
            dto.setStatus(entity.getStatus());

            List<CocDetailSampleRespDTO> samples = sampleDao.selectSamplesByCocId(cocId);
            dto.setSamples(samples);

            return dto;

        } finally {
            DBUtil.closeConnection();
        }
    }

    // --- Helpers ---

    private Date toDate(LocalDateTime ldt) {
        // if (ldt == null) return null;
        // return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        // Using SQL's Timestamp instead
        return ldt == null ? null : Timestamp.valueOf(ldt);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}