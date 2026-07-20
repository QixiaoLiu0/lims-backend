package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.LookupTestTypeRespDTO;
import java.util.List;

public class LookupDao extends BaseJdbcDao {
	
    // This method queries the test_type where is_active = 1, specifically for use with  frontend dropdowns
    public List<LookupTestTypeRespDTO> selectActiveTestTypes() throws Exception {
    	String sql = "SELECT test_type_id, type_name, description, required_volume, bg_color, icon_color, border_color" +
                     "FROM test_type WHERE is_active = 1 ORDER BY type_name";

        return executeQuery(sql, LookupTestTypeRespDTO.class);
    }
}