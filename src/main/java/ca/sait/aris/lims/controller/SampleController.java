package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.SampleDetailRespDTO;
import ca.sait.aris.lims.dto.resp.SampleIdRespDTO;
import ca.sait.aris.lims.service.SampleService;

//sprint 3
public class SampleController {

    private final SampleService sampleService = new SampleService();

    // API 5: Append Sample to COC
    public RespResult<SampleIdRespDTO> appendSampleToCoc(String cocId, String jsonBody) {
        //TODO
        return null;
    }

    // API 7: Delete Sample
    public RespResult<Object> deleteSample(String sampleId) {
        try {
            sampleService.deleteSample(sampleId);
            return RespResult.success();
        } catch (Exception e) {
            System.err.println("[SampleController] deleteSample failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to delete sample: " + sampleId);
        }
    }

    // API 13: Get Sample Details
    public RespResult<SampleDetailRespDTO> getSampleDetail(String sampleId) {
        //TODO
        return null;
    }
}