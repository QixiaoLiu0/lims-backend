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
    	//TODO
		return null;
    	
        
    }


    // API 2: Get Sample Types
    public RespResult<List<LookupSampleTypeRespDTO>> getSampleTypes() {
    	try {
            List<LookupSampleTypeRespDTO> list = lookupService.getSampleTypes();

            return RespResult.success(list);
        } catch (Exception e) {
            System.err.println("[LookupController] getSampleTypes failed " + e.getMessage());
            e.printStackTrace();

            return RespResult.error("Failed to retrieve sample types");
        }
    }
}