package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.entity.Coc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ca.sait.aris.lims.dto.resp.DashboardCocRespDTO;
import ca.sait.aris.lims.util.DBUtil;

public class CocDao extends BaseJdbcDao {
	// Do not insert the 'status' field(It has default value while inserting).
    public void insertCoc(Coc coc) throws Exception {
        String sql = "INSERT INTO coc (coc_id, coc_number, project_name, report_to_name1, report_to_email1, " +
                "report_to_name2, report_to_email2, date_required, is_rush, date_for_rush, received_by, " +
                "received_time, relinquished_by, relinquished_time, number_of_containers, special_instructions, " +
                "created_by_user_id, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql,
                coc.getCocId(),
                coc.getCocNumber(),
                coc.getProjectName(),
                coc.getReportToName1(),
                coc.getReportToEmail1(),
                coc.getReportToName2(),
                coc.getReportToEmail2(),
                toTimestamp(coc.getDateRequired()),
                coc.getIsRush(),
                toTimestamp(coc.getDateForRush()),
                coc.getReceivedBy(),
                toTimestamp(coc.getReceivedTime()),
                coc.getRelinquishedBy(),
                toTimestamp(coc.getRelinquishedTime()),
                coc.getNumberOfContainers(),
                coc.getSpecialInstructions(),
                coc.getCreatedByUserId(),
                toTimestamp(coc.getCreatedAt())
        );
    }

    public void deleteCocById(String cocId) throws Exception {
    	String sql = "DELETE FROM coc WHERE coc_id = ?";
        executeUpdate(sql, cocId);
    }

    public Coc selectCocById(String cocId) throws Exception {
    	//TODO
		return null;
    }

    // Aggregate SQL Defense N+1: Retrieve statistics for all COCs and their subordinate Tests in one go.
    public List<DashboardCocRespDTO> selectDashboardCocs() throws Exception {
        // Raw JDBC (not the generic executeQuery/reflection helper) because
        // COUNT()/SUM() return BIGINT (Long) in MySQL, which would throw exception when
        // reflection tries to set them into this DTO's Integer fields.
        // rs.getInt(...) handles that conversion safely.
        String sql = "SELECT c.coc_id, c.coc_number, c.project_name, c.status, c.received_time, c.date_required, " +
                "COUNT(t.test_id) AS total_tests, " +
                "SUM(CASE WHEN t.status = 'Completed' THEN 1 ELSE 0 END) AS completed_tests " +
                "FROM coc c " +
                "LEFT JOIN sample s ON s.coc_id = c.coc_id " +
                "LEFT JOIN test t ON t.sample_id = s.sample_id " +
                "GROUP BY c.coc_id, c.coc_number, c.project_name, c.status, c.received_time, c.date_required " +
                "ORDER BY c.received_time DESC";

        List<DashboardCocRespDTO> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                DashboardCocRespDTO dto = new DashboardCocRespDTO();
                dto.setCocId(rs.getString("coc_id"));
                dto.setCocNumber(rs.getString("coc_number"));
                dto.setProjectName(rs.getString("project_name"));
                dto.setStatus(rs.getString("status"));

                Timestamp receivedTime = rs.getTimestamp("received_time");
                dto.setReceivedTime(receivedTime == null ? null : receivedTime.toLocalDateTime());

                Timestamp dateRequired = rs.getTimestamp("date_required");
                dto.setDateRequired(dateRequired == null ? null : dateRequired.toLocalDateTime());

                dto.setTotalTests(rs.getInt("total_tests"));
                dto.setCompletedTests(rs.getInt("completed_tests"));
                // samples list intentionally left unset here - populated by the
                // Service layer via a separate batched query (N+1 defense).
                list.add(dto);
            }
        }
        return list;
    }

    // 'status' Bubble Update
    public void updateCocStatus(String cocId, String status) throws Exception {
    	//TODO
    }
}
