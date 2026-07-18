package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dto.req.CocSaveReqDTO;
import ca.sait.aris.lims.dto.resp.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class CocService {
    

    // API 4: Multi-table, atomic aggregation creation (Hierarchical Aggregate Creation)
    public String createCoc(CocSaveReqDTO req) throws Exception {
    	//TODO
		return null;
        
    }

    // API 3: Cascade Physical Deletion
    public void deleteCoc(String cocId) throws Exception {
        //TODO
    }

    // API 11: Avoids N+1 memory aggregation queries for the Dashboard
    public List<DashboardCocRespDTO> getDashboardCocs() throws Exception {
		
        //TODO
    	
    	return null;
    }

    // API 12: Get COC Details
    public CocDetailRespDTO getCocDetail(String cocId) throws Exception {
    	//TODO
		return null;
       
    }

 // --- Helpers ---

    /**
     * 1. For write operations: The LocalDateTime passed to the DTO is converted into the java.util.Date object required by the persistent layer entity.
     */
    private Date toDate(LocalDateTime ldt) {
        if (ldt == null) return null;
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 2. Used for read operations: Retrieving a java.util.Date from a database/Entity and converting it to the required LocalDateTime for a DTO.
     */
    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}