package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.CocDetailSampleRespDTO;
import ca.sait.aris.lims.entity.Sample;

import ca.sait.aris.lims.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import ca.sait.aris.lims.dao.BaseJdbcDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SampleDao extends BaseJdbcDao {

    // Do not insert the status field
    // toTimestamp func is implemented in BaseJdbcDao file inside SCRUM-85 branch/ BaseJbdcDao imported - should work when merged I guess
    public void insertSample(Sample sample) throws Exception {
        String sql = "INSERT INTO sample (sample_id, coc_id, sample_type_id, sample_client_id, sampled_time, " +
                "sampling_point, matrix, number_of_containers, remarks, initial_volume, remaining_volume, " +
                "created_at, is_filtered, is_preserved, is_filtered_and_preserved) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql,
                sample.getSampleId(),
                sample.getCocId(),
                sample.getSampleTypeId(),
                sample.getSampleClientId(),
                toTimestamp(sample.getSampledTime()),  // <---- toTimestamp func is implemented in BaseJdbcDao file inside SCRUM-85 branch
                sample.getSamplingPoint(),
                sample.getMatrix(),
                sample.getNumberOfContainers(),
                sample.getRemarks(),
                sample.getInitialVolume(),
                sample.getRemainingVolume(),
                toTimestamp(sample.getCreatedAt()),
                sample.getIsFiltered(),
                sample.getIsPreserved(),
                sample.getIsFilteredAndPreserved()
        );

    }

    public void deleteSampleById(String sampleId) throws Exception {
    	String sql = "DELETE FROM sample WHERE sample_id = ?";
        executeUpdate(sql, sampleId);
    }

    public Sample selectSampleById(String sampleId) throws Exception {
    	//TODO
		return null;
        
    }

    // Aggregate SQL: Query the Sample list and Test info statistics for the coc details page.
    public List<CocDetailSampleRespDTO> selectSamplesByCocId(String cocId) throws Exception {
        String sql = "SELECT s.sample_id, s.sample_client_id, s.matrix, s.sampling_point, s.sampled_time, " +
                "s.initial_volume, s.remaining_volume, s.status, COUNT(t.test_id) AS total_tests " +
                "FROM sample s " +
                "LEFT JOIN test t ON t.sample_id = s.sample_id " +
                "WHERE s.coc_id = ? " +
                "GROUP BY s.sample_id, s.sample_client_id, s.matrix, s.sampling_point, s.sampled_time, " +
                "s.initial_volume, s.remaining_volume, s.status " +
                "ORDER BY s.created_at";

        List<CocDetailSampleRespDTO> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cocId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CocDetailSampleRespDTO dto = new CocDetailSampleRespDTO();
                    dto.setSampleId(rs.getString("sample_id"));
                    dto.setSampleClientId(rs.getString("sample_client_id"));
                    dto.setMatrix(rs.getString("matrix"));
                    dto.setSamplingPoint(rs.getString("sampling_point"));

                    Timestamp sampledTime = rs.getTimestamp("sampled_time");
                    dto.setSampledTime(sampledTime == null ? null : sampledTime.toLocalDateTime());

                    dto.setInitialVolume(rs.getBigDecimal("initial_volume"));
                    dto.setRemainingVolume(rs.getBigDecimal("remaining_volume"));
                    dto.setStatus(rs.getString("status"));
                    dto.setTotalTests(rs.getInt("total_tests"));
                    list.add(dto);
                }
            }
        }
        return list;
    }

    // Defense against N+1: Use the IN statement to retrieve all samples from multiple COCs at once.
    public List<Sample> selectSamplesByCocIds(List<String> cocIds) throws Exception {

        if (cocIds == null || cocIds.isEmpty()) {
            return new ArrayList<>();
        }

        String placeholders = String.join(",", Collections.nCopies(cocIds.size(), "?"));
        String sql = "SELECT sample_id, coc_id, sample_type_id, sample_client_id, sampled_time, sampling_point, " +
                "matrix, number_of_containers, remarks, initial_volume, remaining_volume, created_at, " +
                "is_filtered, is_preserved, is_filtered_and_preserved, status " +
                "FROM sample WHERE coc_id IN (" + placeholders + ")";

        return executeQuery(sql, Sample.class, cocIds.toArray());

    }

    // status Bubble Update
    public void updateSampleStatus(String sampleId, String status) throws Exception {
    	//TODO
    }

    // Check if there are any incomplete Samples under a given COC.
    public int countIncompleteSamplesByCocId(String cocId) throws Exception {
    	//TODO
		return 0;
        
    }

    // AI chatbot support: retrieve samples filtered by status
    public List<Sample> selectSamplesByStatus(String status) throws Exception {
        String sql = "SELECT sample_id, coc_id, sample_type_id, sample_client_id, sampled_time, sampling_point, " +
                "matrix, number_of_containers, remarks, initial_volume, remaining_volume, created_at, " +
                "is_filtered, is_preserved, is_filtered_and_preserved, status " +
                "FROM sample WHERE status = ?";
        return executeQuery(sql, Sample.class, status);
    }
}