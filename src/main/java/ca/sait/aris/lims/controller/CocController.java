package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.req.CocSaveReqDTO;
import ca.sait.aris.lims.dto.resp.CocDetailRespDTO;
import ca.sait.aris.lims.dto.resp.CocIdRespDTO;
import ca.sait.aris.lims.dto.resp.DashboardCocRespDTO;
import ca.sait.aris.lims.service.CocService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.gson.Gson;

//sprint 3
public class CocController {
	private final CocService cocService = new CocService();
	private final Gson gson;
    
    public CocController(Gson gson){
    	this.gson = gson;
    }


    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                            LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .create();


    // API 4: Create COC
    public RespResult<CocIdRespDTO> createCoc(String jsonBody) {
        try {
            CocSaveReqDTO reqDto = GSON.fromJson(jsonBody, CocSaveReqDTO.class);
            String newCocId = cocService.createCoc(reqDto);
            return RespResult.success(new CocIdRespDTO(newCocId));
        } catch (Exception e) {
            System.err.println("[CocController] createCoc failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to create COC.");
        }
    }

    // API 3: Delete COC
    public RespResult<Object> deleteCoc(String cocId) {
        try {
            cocService.deleteCoc(cocId);

            return RespResult.success();

        } catch (Exception e) {
            System.err.println("[CocController] deleteCoc failed: " + e.getMessage());
            e.printStackTrace();

            return RespResult.error("Failed to delete COC: " + cocId);
        }
    }

    // API 11: Get COCs for Dashboard
    public RespResult<List<DashboardCocRespDTO>> getDashboardCocs() {
        try {
            List<DashboardCocRespDTO> list = cocService.getDashboardCocs();

            return RespResult.success(list);

        } catch (Exception e) {
            System.err.println("[CocController] getDashboardCocs failed: " + e.getMessage());
            e.printStackTrace();

            return RespResult.error("Failed to retrieve dashboard COCs.");
        }
    }

    // API 12: Get COC Details
    public RespResult<CocDetailRespDTO> getCocDetail(String cocId) {
        //TODO
        return null;
    }
}