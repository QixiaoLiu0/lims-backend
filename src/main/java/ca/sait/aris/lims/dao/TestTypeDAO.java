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
        String sql = "INSERT INTO test_type (type_name, description, required_volume, bg_color, icon_color, border_color, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int generatedId = executeInsertAndReturnKey(sql,
                testTypeEntity.getTypeName(),
                testTypeEntity.getDescription(),
                testTypeEntity.getRequiredVolume(),
                testTypeEntity.getBgColor(),
                testTypeEntity.getIconColor(),
                testTypeEntity.getBorderColor(),
                testTypeEntity.getIsActive());

        return generatedId;

    }

    /**
     * 2. Query all records in the main table
     */
    public List<TestType> selectAllTestTypes() throws Exception {
        String sql = "SELECT test_type_id, type_name, description, required_volume, bg_color, icon_color, border_color, is_active " +
                "FROM test_type";

        return executeQuery(sql, TestType.class);
    }

    /**
     * 3. Query for a single record by ID
     */
    public TestType selectTestTypeById(int testTypeId) throws Exception {
        String sql = "SELECT test_type_id, type_name, description, required_volume, bg_color, icon_color, border_color, is_active " +
                "FROM test_type WHERE test_type_id = ?";

        return executeQueryForObject(sql, TestType.class, testTypeId);
    }

    /**
     * 4. Update the basic fields of the main table
     */
    public boolean updateTestType(TestType entity) throws Exception {
        String sql = "UPDATE test_type SET type_name = ?, description = ?, required_volume = ?, " +
                "bg_color = ?, icon_color = ?, border_color = ?, is_active = ? " +
                "WHERE test_type_id = ?";

        int rowsAffected = executeUpdate(sql,
                entity.getTypeName(),
                entity.getDescription(),
                entity.getRequiredVolume(),
                entity.getBgColor(),
                entity.getIconColor(),
                entity.getBorderColor(),
                entity.getIsActive(),
                entity.getTestTypeId());

        return rowsAffected > 0;
    }
}