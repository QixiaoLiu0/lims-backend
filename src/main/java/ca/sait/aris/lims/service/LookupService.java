package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dto.resp.LookupSampleTypeRespDTO;
import ca.sait.aris.lims.dto.resp.LookupTestTypeRespDTO;
import ca.sait.aris.lims.dao.LookupDao;
import ca.sait.aris.lims.util.DBUtil;

import java.util.List;

public class LookupService {

    private final LookupDao lookupDao = new LookupDao();
    /**
     * Retrieve active test types (read-only)
     */
    public List<LookupTestTypeRespDTO> getActiveTestTypes() throws Exception {
        try{
            return lookupDao.selectActiveTestTypes();
        } finally {
            DBUtil.closeConnection();
        }
    }

    public List<LookupSampleTypeRespDTO> getSampleTypes() throws Exception {
    	//TODO
		return null;
        
    }
}