package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.entity.Coc;

import java.util.List;

import ca.sait.aris.lims.dto.resp.DashboardCocRespDTO;

public class CocDao extends BaseJdbcDao {
	// Do not insert the 'status' field(It has default value while inserting).
    public void insertCoc(Coc coc) throws Exception {
    	//TODO
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
