package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.req.TestTypeSaveReqDTO;
import ca.sait.aris.lims.dto.resp.TestTypeListRespDTO;
import ca.sait.aris.lims.dto.resp.TestTypeRespDTO;
import ca.sait.aris.lims.service.TestTypeService;

import java.util.List;

/**
 * Pure Java Controller for handling Analytical Test Type configurations.
 * Completely decoupled from Tomcat Servlet container dependencies.
 */
public class TestTypeController {

    private final TestTypeService testTypeService = new TestTypeService();

    
    /**
     * 1. Create Test Type (POST /api/test-types)
     */
    public RespResult<Integer> createTestType(TestTypeSaveReqDTO reqDto) {
    	//TODO
		return null;
	}

    /**
     * 2. Get Test Type List (GET /api/test-types)
     */
    public RespResult<List<TestTypeListRespDTO>> getTestTypeList() {
    	//TODO
		return null;
        
    }

    /**
     * 3. Get Test Type Detail (GET /api/test-types/{id})
     */
    public RespResult<TestTypeRespDTO> getTestTypeDetail(int testTypeId) {
    	//TODO
		return null;
    }

    /**
     * 4. Update Test Type (POST /api/test-types/{id})
     * Changed return token to RespResult<Object> to strictly match API spec ("data": null).
     */
    public RespResult<Object> updateTestType(int testTypeId, TestTypeSaveReqDTO reqDto) {
    	//TODO
		return null;
    }
}