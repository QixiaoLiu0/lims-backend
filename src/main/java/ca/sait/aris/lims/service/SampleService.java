package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.ParameterDAO;
import ca.sait.aris.lims.dao.ResultDao;
import ca.sait.aris.lims.dao.SampleDao;
import ca.sait.aris.lims.dao.TestDao;
import ca.sait.aris.lims.dto.req.SampleSaveReqDTO;
import ca.sait.aris.lims.dto.resp.SampleDetailRespDTO;
import ca.sait.aris.lims.entity.Parameter;
import ca.sait.aris.lims.entity.Sample;
import ca.sait.aris.lims.entity.Test;
import ca.sait.aris.lims.util.DBUtil;
import ca.sait.aris.lims.util.UserContext;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SampleService {

    private final SampleDao sampleDao = new SampleDao();
    private final TestDao testDao = new TestDao();
    private final ParameterDAO parameterDao = new ParameterDAO();
    private final ResultDao resultDao = new ResultDao();

    // API 5: Append Sample to coc
    public String appendSampleToCoc(String cocId, SampleSaveReqDTO req) throws Exception {

        Connection conn = DBUtil.getConnection();
        String currentUserId = UserContext.getUserId();
        Date now = new Date();
        Timestamp nowTs = new Timestamp(now.getTime());

        try {
            conn.setAutoCommit(false);

            Sample sample = new Sample();
            sample.setSampleId(UUID.randomUUID().toString());
            sample.setCocId(cocId);
            sample.setSampleTypeId(req.getSampleTypeId());
            sample.setSampleClientId(req.getSampleClientId());
            sample.setSampledTime(toDate(req.getSampledTime()));
            sample.setSamplingPoint(req.getSamplingPoint());
            sample.setMatrix(req.getMatrix());
            sample.setNumberOfContainers(req.getNumberOfContainers());
            sample.setRemarks(req.getRemarks());
            sample.setInitialVolume(req.getInitialVolume());
            sample.setRemainingVolume(req.getRemainingVolume());
            sample.setCreatedAt(now);
            sample.setIsFiltered(req.getIsFiltered());
            sample.setIsPreserved(req.getIsPreserved());
            sample.setIsFilteredAndPreserved(req.getIsFilteredAndPreserved());

            sampleDao.insertSample(sample);

            // ---- ASSUMPTION: also creates nested Tests + Result placeholders ----
            // Ticket says "append a single Sample," but SampleSaveReqDTO includes
            // testTypeIds, so this section acts on it if present. If your team
            // confirms this endpoint should ONLY create the bare sample row,
            // delete this whole block down to the "---- END ASSUMPTION ----" line.
            if (req.getTestTypeIds() != null) {
                List<Object[]> placeholderRows = new ArrayList<>();

                for (Integer testTypeId : req.getTestTypeIds()) {
                    int nextRunNumber = testDao.getMaxRunNumber(sample.getSampleId(), testTypeId) + 1;

                    Test test = new Test();
                    test.setTestId(UUID.randomUUID().toString());
                    test.setSampleId(sample.getSampleId());
                    test.setTestTypeId(testTypeId);
                    test.setCreatedAt(now);
                    test.setRunNumber(nextRunNumber);
                    test.setRetestReason(null);

                    testDao.insertTest(test);

                    List<Parameter> parameters = parameterDao.selectParametersByTestTypeId(testTypeId);
                    for (Parameter parameter : parameters) {
                        placeholderRows.add(new Object[]{
                                UUID.randomUUID().toString(),
                                test.getTestId(),
                                parameter.getParameterId(),
                                nowTs,
                                currentUserId,
                                ""  // placeholder qualifier - confirm default with team
                        });
                    }
                }

                resultDao.batchInsertPlaceholders(placeholderRows);
            }
            // ---- END ASSUMPTION ----

            conn.commit();
            return sample.getSampleId();

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

    // API 7: Cascade Physical delete Sample
    public void deleteSample(String sampleId) throws Exception {
    	//TODO
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