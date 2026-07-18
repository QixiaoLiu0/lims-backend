package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.LookupTestTypeRespDTO;
import java.util.List;

public class LookupDao extends BaseJdbcDao {
	
    // This method queries the test_type where is_active = 1, specifically for use with  frontend dropdowns
    public List<LookupTestTypeRespDTO> selectActiveTestTypes() throws Exception {
    	//TODO
		return null;
    }
}