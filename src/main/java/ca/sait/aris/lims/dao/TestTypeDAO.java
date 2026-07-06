package ca.sait.aris.lims.dao;

import java.util.List;

import ca.sait.aris.lims.dto.req.TestTypeSaveReqDTO;
import ca.sait.aris.lims.dto.resp.TestTypeListRespDTO;
import ca.sait.aris.lims.dto.resp.TestTypeRespDTO;
import ca.sait.aris.lims.entity.TestType;

/**
 * 
 * main table: test_type
 */
public class TestTypeDAO extends BaseJdbcDao {

    /**
     * 1. Insert a record into the main table and return the auto-incrementing primary key.
     */
    public int insertTestType(TestType testTypeEntity) throws Exception {
    	//TODO
		return 0;
        
    }

    /**
     * 2. Query all records in the main table
     */
    public List<TestType> selectAllTestTypes() throws Exception {
    	//TODO
		return null;
    	// As long as the column names (underscores) returned by the SELECT query match the attribute names (camel case) of the Entity,
    	//executeQuery will automatically assemble the List for you using reflection at the underlying level.
        
    }

    /**
     * 3. Query for a single record by ID
     */
    public TestType selectTestTypeById(int testTypeId) throws Exception {
    	//TODO
		return null;
    	
    }

    /**
     * 4. Update the basic fields of the main table
     */
    public boolean updateTestType(TestType entity) throws Exception {
    	//TODO
		return false;
        
    }
}
