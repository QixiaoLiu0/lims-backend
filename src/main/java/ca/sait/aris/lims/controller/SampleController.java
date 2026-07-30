package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.dto.req.SampleSaveReqDTO;
import com.google.gson.Gson;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.SampleDetailRespDTO;
import ca.sait.aris.lims.dto.resp.SampleIdRespDTO;
import ca.sait.aris.lims.service.SampleService;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//sprint 3
public class SampleController {
	private final Gson gson;
    
    public SampleController(Gson gson){
    	this.gson = gson;
    }

    private final SampleService sampleService = new SampleService();

    // Duplicate ?
    //private final SampleService sampleService = new SampleService();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                            LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .create();


    // API 5: Append Sample to COC
    public RespResult<SampleIdRespDTO> appendSampleToCoc(String cocId, String jsonBody) {
        try {
            SampleSaveReqDTO reqDto = GSON.fromJson(jsonBody, SampleSaveReqDTO.class);

            String newSampleId = sampleService.appendSampleToCoc(cocId, reqDto);

            return RespResult.success(new SampleIdRespDTO(newSampleId));
        } catch (Exception e) {

            System.err.println("[SampleController] appendSampleToCoc failed: " + e.getMessage());

            e.printStackTrace();

            return RespResult.error("Failed to append sample to COC.");
        }
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