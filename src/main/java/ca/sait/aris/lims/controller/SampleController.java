package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.SampleDetailRespDTO;
import ca.sait.aris.lims.dto.resp.SampleIdRespDTO;

//sprint 3
public class SampleController {

    // API 5: Append Sample to COC
    public RespResult<SampleIdRespDTO> appendSampleToCoc(String cocId, String jsonBody) {
    	//TODO
		return null;
        
    }

    // API 7: Delete Sample
    public RespResult<Object> deleteSample(String sampleId) {
    	//TODO
		return null;
        
    }

    // API 13: Get Sample Details
    public RespResult<SampleDetailRespDTO> getSampleDetail(String sampleId) {
    	//TODO
		return null;
        
    }
}