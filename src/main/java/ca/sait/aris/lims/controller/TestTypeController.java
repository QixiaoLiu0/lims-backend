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
    	try {
            int newId = testTypeService.createTestType(reqDto);
            return RespResult.success(newId);
        } catch (IllegalArgumentException e) {
            // Bad input from client
            return RespResult.error(400, e.getMessage());
        } catch (Exception e) {
            System.err.println("[TestTypeController] createTestType failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to create test type.");
        }
	}

    /**
     * 2. Get Test Type List (GET /api/test-types)
     */
    public RespResult<List<TestTypeListRespDTO>> getTestTypeList() {
    	try {
            List<TestTypeListRespDTO> list = testTypeService.listTestTypes();
            return RespResult.success(list);
        } catch (Exception e){
           System.err.println("[TestTypeController] getTestTypeList failed: " + e.getMessage());
           e.printStackTrace();
           return RespResult.error("Failed to retrieve test type list.");
        }
        
    }

    /**
     * 3. Get Test Type Detail (GET /api/test-types/{id})
     */
    public RespResult<TestTypeRespDTO> getTestTypeDetail(int testTypeId) {
    	try {
            TestTypeRespDTO detail = testTypeService.getTestTypeDetail(testTypeId);
            if (detail == null) {
                return RespResult.error(404, "Test type not found: " + testTypeId);
            }
            return RespResult.success(detail);
        } catch (Exception e) {
            System.err.println("[TestTypeController] getTestTypeDetail failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to retrieve test type data.");
        }

    }

    /**
     * 4. Update Test Type (POST /api/test-types/{id})
     * Changed return token to RespResult<Object> to strictly match API spec ("data": null).
     */
    public RespResult<Object> updateTestType(int testTypeId, TestTypeSaveReqDTO reqDto) {
    	try {
            boolean updated = testTypeService.updateTestType(testTypeId, reqDto);
            if (!updated) {
                return RespResult.error(404, "Test type not found: " + testTypeId);
            }
            return RespResult.success();
        } catch (IllegalArgumentException e) {
            // Bad input from client
            return RespResult.error(400, e.getMessage());
        } catch (Exception e){
            System.err.println("[TestTypeController] updateTestType failed: " + e.getMessage());
            e.printStackTrace();
            return RespResult.error("Failed to update test type.");
        }
    }
}