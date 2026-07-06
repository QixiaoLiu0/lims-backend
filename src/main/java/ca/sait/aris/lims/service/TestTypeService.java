package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.ParameterDAO;
import ca.sait.aris.lims.dao.TestTypeDAO;
import ca.sait.aris.lims.dto.req.ParameterReqDTO;
import ca.sait.aris.lims.dto.req.TestTypeSaveReqDTO;
import ca.sait.aris.lims.dto.resp.ParameterRespDTO;
import ca.sait.aris.lims.dto.resp.TestTypeListRespDTO;
import ca.sait.aris.lims.dto.resp.TestTypeRespDTO;
import ca.sait.aris.lims.entity.Parameter;
import ca.sait.aris.lims.entity.TestType;
import ca.sait.aris.lims.util.DBUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * core business layer (Service Layer)
 * It manages the life cycle of database connections, defines strict transaction boundaries, and ensures the atomicity of multi-table cascading operations.
 */
public class TestTypeService {

    private final TestTypeDAO testTypeDao = new TestTypeDAO();
    private final ParameterDAO parameterDao = new ParameterDAO();

    
    /**
     * 1. Added new test types (including transaction binding: insert into main table -> retrieve ID -> batch insert into slave table)
     */
    public int createTestType(TestTypeSaveReqDTO dto) throws Exception {
		
    	// Get the database connection currently bound to the thread

    	// Start a transaction (disable auto-commit)

    	// Complete the DTO to Entity translation in the Service layer

    	// Commit the transaction

    	// If an exception occurs, catch the exception and roll back the entire transaction

    	// Return the connection to HikariCP and execute ThreadLocal.remove() to prevent memory leaks
    	
    	//TODO
    	
    	
    	return 0;
        
    }

    /**
     * 2. Retrieve a list of test types (read-only operation, no transaction required, but connection loop management is still necessary).
     */
    public List<TestTypeListRespDTO> listTestTypes() throws Exception {
		
    	// 1. Retrieve the original list of underlying main table entities from the DAO.

    	// 2. Prepare the anti-corruption layer conversion container: `List<TestTypeListRespDTO> dtos = new ArrayList<>();`

    	// 3. Instantiate the subclass DTO (fat DTO) containing the `parameters` field.

    	// A. Translate the basic information of the main table.

    	// B. Use the main table ID to query the original list of underlying secondary table entities from the DAO: `List<Parameter> paramEntities = parameterDao.selectParametersByTestTypeId(entity.getTestTypeId());`

    	// C. Translate the nested secondary table information.

    	// D. Mount the translated secondary table DTO to the main DTO.

    	// E. Add to the final return list (upcasting: `TestTypeRespDTO` is added to `List<TestTypeListRespDTO>`).
                   
    	return null;
    }

    /**
     * 3. Retrieve details of a single test type (including main table query + sub-table parameter query)
     */
    public TestTypeRespDTO getTestTypeDetail(int testTypeId) throws Exception {
		
    	// 1. Query the main table Entity

    	// 2. Translate the main table Entity to DTO

    	// 3. Query the list of subordinate table Entities

    	// 4. Translate the subordinate table Entity to DTO (adapted with the new limit field)

    	// 5. Mount the list of child table DTOs

    	return null;
    }

    /**
     * 4. Update test types (including transaction bindings: master table update + slave table differential update)
     */
    public boolean updateTestType(int testTypeId, TestTypeSaveReqDTO dto) throws Exception {
    	//TODO
		return false;
    	
    }
    	
}