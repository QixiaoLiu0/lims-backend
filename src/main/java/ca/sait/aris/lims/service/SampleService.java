package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dto.req.SampleSaveReqDTO;
import ca.sait.aris.lims.dto.resp.SampleDetailRespDTO;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SampleService {

    // API 5: Append Sample to coc
    public String appendSampleToCoc(String cocId, SampleSaveReqDTO req) throws Exception {
    	//TODO
		return cocId;
        
    }

    // API 7: Cascade Physical delete Sample
    public void deleteSample(String sampleId) throws Exception {
    	//TODO
    }

    // API 13: Get Sample Details
    public SampleDetailRespDTO getSampleDetail(String sampleId) throws Exception {
    	//TODO
		return null;
        
    }
    
    
    
    
    
    
    
    
    
    //helpers
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