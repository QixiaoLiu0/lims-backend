package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.CocDetailRespDTO;
import ca.sait.aris.lims.dto.resp.CocIdRespDTO;
import ca.sait.aris.lims.dto.resp.DashboardCocRespDTO;

import java.util.List;

import com.google.gson.Gson;

//sprint 3
public class CocController {
	
	private final Gson gson;
    
    public CocController(Gson gson){
    	this.gson = gson;
    }

    // API 4: Create COC
    public RespResult<CocIdRespDTO> createCoc(String jsonBody) {
    	//TODO
		return null;
        
    }

    // API 3: Delete COC
    public RespResult<Object> deleteCoc(String cocId) {
    	//TODO
		return null;
        
    }

    // API 11: Get COCs for Dashboard
    public RespResult<List<DashboardCocRespDTO>> getDashboardCocs() {
    	//TODO
		return null;
        
    }

    // API 12: Get COC Details
    public RespResult<CocDetailRespDTO> getCocDetail(String cocId) {
    	//TODO
		return null;
        
    }
}