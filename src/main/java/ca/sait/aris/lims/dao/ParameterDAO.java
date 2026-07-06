package ca.sait.aris.lims.dao;

import java.util.List;

import ca.sait.aris.lims.dto.req.ParameterReqDTO;
import ca.sait.aris.lims.dto.resp.ParameterRespDTO;
import ca.sait.aris.lims.entity.Parameter;

/**
 * Sub-table Data Access Layer (DAO)
 * Responsible for CRUD operations on the test parameter table, supporting differential update logic in the Service layer.
 */
public class ParameterDAO extends BaseJdbcDao {

    /**
     * 1. Query the list of all parameters belonging to the main table ID.
     */
    public List<Parameter> selectParametersByTestTypeId(int testTypeId) throws Exception {
    	//TODO
		return null;
    }

    /**
     * 2. Insert a new parameter record
     */
    public void insertParameter(Parameter entity) throws Exception {
    	//TODO
    }

    /**
     * 3. Precisely update a single existing parameter (supports differential algorithms).
     */
    public void updateParameter(Parameter existingEntity) throws Exception {
    	//TODO
    }

    /**
     * 4. Precisely delete individual parameters based on primary key (supports differential algorithm)
     */
    public void deleteParameterById(Integer removedId) throws Exception {
    	//TODO
    }

    /**
     * 5. (Backup) Forcefully clear all parameters under a specific test type
     */
    public void deleteParametersByTestTypeId(int testTypeId) throws Exception {
    	//TODO
    }
}
