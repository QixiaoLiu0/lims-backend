package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.resp.CocDetailRespDTO;
import ca.sait.aris.lims.dto.resp.CocIdRespDTO;
import ca.sait.aris.lims.dto.resp.DashboardCocRespDTO;
import ca.sait.aris.lims.service.CocService;

import java.util.List;

//sprint 3
public class CocController {

    private final CocService cocService = new CocService();

    // API 4: Create COC
    public RespResult<CocIdRespDTO> createCoc(String jsonBody) {
        //TODO
        return null;
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
        //TODO
        return null;
    }

    // API 12: Get COC Details
    public RespResult<CocDetailRespDTO> getCocDetail(String cocId) {
        //TODO
        return null;
    }
}