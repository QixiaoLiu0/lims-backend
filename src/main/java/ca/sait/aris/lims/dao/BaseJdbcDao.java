package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.util.DBUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Abstract Data Access Object
 * Encapsulate template code and use reflection to automatically map ResultSet to Java POJO.
 * Chris Jul.14 updates: integrates robust type adaptation patches and a strict fail-fast error handling strategy.
 */
public abstract class BaseJdbcDao {

    /**
     * 1.Generic update method (INSERT / UPDATE / DELETE)
     */
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = DBUtil.getConnection();
        //  1. Never call `close()` here. The lifecycle of a Connection is managed by the Service layer's ThreadLocal transaction. Only release the 'stmt' here.
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate();
        }
    }

    /**
     * 2. Generic insert and return of auto-incrementing primary key (specifically for the Create scenario)
     */
    protected int executeInsertAndReturnKey(String sql, Object... params) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                	// Returns the underlying auto-incrementing ID (e.g., test_type_id)
                    return rs.getInt(1);
                }
            }
            throw new SQLException("Insertion succeeded, but failed to retrieve generated key.");
        }
    }

    /**
     * 3. Generic query list method (using reflection to assemble POJOs)
     */
    protected <T> List<T> executeQuery(String sql, Class<T> clazz, Object... params) throws SQLException {
        List<T> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    T obj = clazz.getDeclaredConstructor().newInstance();

                    for (int i = 1; i <= columnCount; i++) {
                        // Use getColumnLabel to support AS alias syntax in SQL.
                        String columnName = metaData.getColumnLabel(i);
                        Object columnValue = rs.getObject(i);

                        if (columnValue != null) {
                            String fieldName = underscoreToCamelCase(columnName);//Camel mapping
                            
                            try {
                                Field field = findField(clazz, fieldName);
                                
                                if (field == null) {continue; }
                                
                                Class<?> fieldType = field.getType();
                                
                                // Explicitly skip collection class properties to prevent DTO nested List crashes
                                if (Collection.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(fieldType)) {
                                    continue;
                                }
                                
                                // === Type Adaptation Patches === 
                                // 1. Date Type Adaptation
                                if (java.util.Date.class.isAssignableFrom(fieldType)) {
                                    if (columnValue instanceof java.time.LocalDateTime) {
                                        columnValue = java.util.Date.from(((java.time.LocalDateTime) columnValue).atZone(java.time.ZoneId.systemDefault()).toInstant());
                                    } else if (columnValue instanceof java.time.LocalDate) {
                                        columnValue = java.util.Date.from(((java.time.LocalDate) columnValue).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
                                    }
                                } else if (fieldType.equals(java.time.LocalDateTime.class)) {
                                    if (columnValue instanceof java.sql.Timestamp) {
                                        columnValue = ((java.sql.Timestamp) columnValue).toLocalDateTime();
                                    } else if (columnValue instanceof java.util.Date) {
                                        columnValue = ((java.util.Date) columnValue).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                                    }
                                }
                                
                                // 2. TINYINT(1)  & Integer & BIGINT & Boolean adaptor
                                if (fieldType.equals(Integer.class)) {
                                    if (columnValue instanceof Boolean) {
                                        columnValue = ((Boolean) columnValue) ? 1 : 0; // TINYINT -> Integer
                                    } else if (columnValue instanceof Number) {
                                        columnValue = ((Number) columnValue).intValue(); // BIGINT -> Integer
                                    }
                                } else if (fieldType.equals(Long.class) && columnValue instanceof Number) {
                                    columnValue = ((Number) columnValue).longValue();
                                } else if (fieldType.equals(Double.class) && columnValue instanceof Number) {
                                    columnValue = ((Number) columnValue).doubleValue(); // BigDecimal -> Double
                                } else if (fieldType.equals(Boolean.class) && columnValue instanceof Integer) {
                                    columnValue = ((Integer) columnValue) == 1; // TINYINT -> Boolean
                                }
                                
                                field.set(obj, columnValue);
                                
                            } catch (IllegalArgumentException e) {
                            		// Fail-fast strategy: print detailed mismatch details and rethrow the exception
                                System.err.println("[Reflection Error] Field: " + fieldName + 
                                        ", Expected: " + clazz.getDeclaredField(fieldName).getType().getName() + 
                                        ", Actual: " + columnValue.getClass().getName());
                                throw e;
                            }
                        }
                    }
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            throw new SQLException("Reflection mapping failed for class: " + clazz.getName(), e);
        }
        return list;
    }

    /**
     * 4. Generic Single Record Query Method
     */
    protected <T> T executeQueryForObject(String sql, Class<T> clazz, Object... params) throws SQLException {
        List<T> list = executeQuery(sql, clazz, params);
        return list.isEmpty() ? null : list.get(0);
    }
    
    /**
     * [Sprint 3 new] High-performance batch update/insert method
     */
    protected int[] executeBatchUpdate(String sql, List<Object[]> paramsList) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Object[] params : paramsList) {
                setParameters(stmt, params);
                stmt.addBatch(); // Add to batch queue
            }
            return stmt.executeBatch(); // Submit to database at once
        }
    }
    
    /**
     * [Sprint 3 Architecture Upgrade] Specifically designed to resolve generic methods for scalar queries such as COUNT and MAX.
     * Perfectly avoids reflection crashes caused by the lack of standard parameterless constructors in 
     * primitive type wrapper classes (Integer, Long).
     */
    protected <T> T executeQueryForScalar(String sql, Class<T> clazz, Object... params) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Object value = rs.getObject(1);
                    if (value == null) {
                        return null;
                    }
                    
                    // Robustness to numeric types and fault tolerance for type conversion (solving the issue of MySQL COUNT returning Long).
                    if (clazz == Integer.class && value instanceof Number) {
                        return clazz.cast(((Number) value).intValue());
                    } else if (clazz == Long.class && value instanceof Number) {
                        return clazz.cast(((Number) value).longValue());
                    }
                    
                    return clazz.cast(value);
                }
                return null;
            }
        } catch (Exception e) {
            throw new SQLException("Scalar query execution failed: " + sql, e);
        }
    }

  
    /**
     * Helper method: Safely inject parameters into PreparedStatement
     */
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    /**
     * Helper:
     * e.g.: test_type_id -> testTypeId, bg_color -> bgColor
     */
    private String underscoreToCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return "";
        }
        StringBuilder camelCase = new StringBuilder();
        boolean nextIsUpper = false;
        
        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                nextIsUpper = true;
            } else {
                if (nextIsUpper) {
                    camelCase.append(Character.toUpperCase(c));
                    nextIsUpper = false;
                } else {
                    camelCase.append(Character.toLowerCase(c));
                }
            }
        }
        return camelCase.toString();
    }
    /**
     * Helper: Searches for a declared field by name, walking up chain if not found on given class directly
     * Prevents silent mapping failures.
     */
    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
            	Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}