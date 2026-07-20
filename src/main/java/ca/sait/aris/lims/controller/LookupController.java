package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.LookupSampleTypeRespDTO;
import ca.sait.aris.lims.dto.resp.LookupTestTypeRespDTO;
import ca.sait.aris.lims.service.LookupService;

import java.util.List;

//sprint 3
public class LookupController {

    private final LookupService lookupService = new LookupService();
    // API 1: Get Active Test Types
    public RespResult<List<LookupTestTypeRespDTO>> getActiveTestTypes() {
    	try{
            List<LookupTestTypeRespDTO> list = lookupService.getActiveTestTypes();
            return RespResult.success(list);
        } catch (Exception e){
            System.err.println("[LookupController] getActiveTestTypes failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to retrieve active test types");
        }
    }

    // API 2: Get Sample Types
    public RespResult<List<LookupSampleTypeRespDTO>> getSampleTypes() {
    	//TODO
		return null;
        
    	
    }
}