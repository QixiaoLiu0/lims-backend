package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.SampleTypeDao;
import ca.sait.aris.lims.dto.resp.LookupSampleTypeRespDTO;
import ca.sait.aris.lims.dto.resp.LookupTestTypeRespDTO;
import ca.sait.aris.lims.util.DBUtil;

import java.util.List;

public class LookupService {

    private final SampleTypeDao sampleTypeDao = new SampleTypeDao();

    public List<LookupTestTypeRespDTO> getActiveTestTypes() throws Exception {
    	//TODO
		return null;
        
    }

    /**
     * Retrieve all sample types (read-only)
     */
    public List<LookupSampleTypeRespDTO> getSampleTypes() throws Exception {
    	try {
            return sampleTypeDao.selectAllSampleTypes();
        } finally {
            DBUtil.closeConnection();
        }
    }
}