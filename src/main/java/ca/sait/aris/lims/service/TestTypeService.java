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
import java.util.List;

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

        // Get database connection currently bound to thread
        Connection conn = DBUtil.getConnection();

        try {
            // Start transaction (disable auto-commit)
            conn.setAutoCommit(false);

            // Complete DTO to Entity translation in Service layer
            TestType entity = new TestType();
            entity.setTypeName(dto.getTypeName());
            entity.setDescription(dto.getDescription());
            entity.setRequiredVolume(dto.getRequiredVolume());
            entity.setBgColor(dto.getBgColor());
            entity.setIconColor(dto.getIconColor());
            entity.setBorderColor(dto.getBorderColor());
            entity.setIsActive(dto.getIsActive());

            int newTestTypeId = testTypeDao.insertTestType(entity);

            if (dto.getParameters() != null) {
                for (ParameterReqDTO paramDto : dto.getParameters()) {
                    Parameter paramEntity = new Parameter();
                    paramEntity.setTestTypeId(newTestTypeId);
                    paramEntity.setParameterName(paramDto.getParameterName());
                    paramEntity.setUnit(paramDto.getUnit());
                    paramEntity.setLimit(paramDto.getLimit());
                    parameterDao.insertParameter(paramEntity);
                }
            }

            // Commit transaction
            conn.commit();
            return newTestTypeId;

        } catch (Exception e) {
            // If exception occurs, catch exception and roll back entire transaction
            try {
                conn.rollback();
            } catch (Exception rollbackEx) {
                System.err.println("[TestTypeService] Rollback failed: " + rollbackEx.getMessage());
            }
            throw e;

        } finally {
            // Return connection to HikariCP and execute ThreadLocal.remove() to prevent memory leaks
            try {
                conn.setAutoCommit(true);
            } catch (Exception ignored) {
            }
            DBUtil.closeConnection();
        }
    }

    /**
     * 2. Retrieve a list of test types (read-only operation, no transaction required, but connection loop management is still necessary).
     */
    public List<TestTypeListRespDTO> listTestTypes() throws Exception {
        try {
            // 1. Retrieve  original list of underlying main table entities from DAO
            List<TestType> entities = testTypeDao.selectAllTestTypes();

            // 2. Prep anti-corruption layer conversion
            List<TestTypeListRespDTO> dtos = new ArrayList<>();

            for (TestType entity : entities) {
                // 3. Instantiate subclass DTO containing `parameters` field
                TestTypeRespDTO dto = new TestTypeRespDTO();

                // 3a. Translate basic information of main table
                dto.setTestTypeId(entity.getTestTypeId());
                dto.setTypeName(entity.getTypeName());
                dto.setDescription(entity.getDescription());
                dto.setRequiredVolume(entity.getRequiredVolume());
                dto.setBgColor(entity.getBgColor());
                dto.setIconColor(entity.getIconColor());
                dto.setBorderColor(entity.getBorderColor());
                dto.setIsActive(entity.getIsActive());

                // 3b. Use main table ID to query original list of secondary table entities from DAO
                List<Parameter> paramEntities = parameterDao.selectParametersByTestTypeId(entity.getTestTypeId());

                // 3c. Translate nested secondary table info
                List<ParameterRespDTO> paramDtos = new ArrayList<>();
                for (Parameter paramEntity : paramEntities) {
                    ParameterRespDTO paramDto = new ParameterRespDTO();
                    paramDto.setParameterId(paramEntity.getParameterId());
                    paramDto.setParameterName(paramEntity.getParameterName());
                    paramDto.setUnit(paramEntity.getUnit());
                    paramDto.setLimit(paramEntity.getLimit());
                    paramDtos.add(paramDto);
                }

                // 3d. Mount translated secondary table DTO to main
                dto.setParameters(paramDtos);

                // 3e. Add to final return list
                dtos.add(dto);
            }

            return dtos;

        } finally {
            DBUtil.closeConnection();
        }
    }

    /**
     * 3. Retrieve details of a single test type (including main table query + sub-table parameter query)
     */
    public TestTypeRespDTO getTestTypeDetail(int testTypeId) throws Exception {
        try {
            // 1. Query main table Entity
            TestType entity = testTypeDao.selectTestTypeById(testTypeId);
            if (entity == null) {
                return null;
            }

            // 2. Translate main table Entity to DTO
            TestTypeRespDTO dto = new TestTypeRespDTO();
            dto.setTestTypeId(entity.getTestTypeId());
            dto.setTypeName(entity.getTypeName());
            dto.setDescription(entity.getDescription());
            dto.setRequiredVolume(entity.getRequiredVolume());
            dto.setBgColor(entity.getBgColor());
            dto.setIconColor(entity.getIconColor());
            dto.setBorderColor(entity.getBorderColor());
            dto.setIsActive(entity.getIsActive());

            // 3. Query list of subordinate table Entities
            List<Parameter> paramEntities = parameterDao.selectParametersByTestTypeId(testTypeId);

            // 4. Translate subordinate table Entity to DTO
            List<ParameterRespDTO> paramDtos = new ArrayList<>();
            for (Parameter paramEntity : paramEntities) {
                ParameterRespDTO paramDto = new ParameterRespDTO();
                paramDto.setParameterId(paramEntity.getParameterId());
                paramDto.setParameterName(paramEntity.getParameterName());
                paramDto.setUnit(paramEntity.getUnit());
                paramDto.setLimit(paramEntity.getLimit());
                paramDtos.add(paramDto);
            }

            // 5. Mount list of child table DTOs
            dto.setParameters(paramDtos);

            return dto;

        } finally {
            DBUtil.closeConnection();
        }
    }

    /**
     * 4. Update test types (including transaction bindings: master table update + slave table differential update)
     */
    public boolean updateTestType(int testTypeId, TestTypeSaveReqDTO dto) throws Exception {

        Connection conn = DBUtil.getConnection();

        try {
            conn.setAutoCommit(false);

            TestType entity = new TestType();
            entity.setTestTypeId(testTypeId);
            entity.setTypeName(dto.getTypeName());
            entity.setDescription(dto.getDescription());
            entity.setRequiredVolume(dto.getRequiredVolume());
            entity.setBgColor(dto.getBgColor());
            entity.setIconColor(dto.getIconColor());
            entity.setBorderColor(dto.getBorderColor());
            entity.setIsActive(dto.getIsActive());

            boolean updated = testTypeDao.updateTestType(entity);

            // NOTE: parameter diffing (insert/update/delete nested parameters) handled separately — will implement when I tackle SCRUM-39.
            // Placeholder call point for now:
            // diffAndApplyParameters(testTypeId, dto.getParameters());

            conn.commit();
            return updated;

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception rollbackEx) {
                System.err.println("[TestTypeService] Rollback failed: " + rollbackEx.getMessage());
            }
            throw e;

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception ignored) {
            }
            DBUtil.closeConnection();
        }
    }
}