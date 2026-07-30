package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.entity.Coc;

import java.util.List;

import ca.sait.aris.lims.dto.resp.DashboardCocRespDTO;

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
    	//TODO
        return null;
    }

    // 'status' Bubble Update
    public void updateCocStatus(String cocId, String status) throws Exception {
    	//TODO
    }
}
