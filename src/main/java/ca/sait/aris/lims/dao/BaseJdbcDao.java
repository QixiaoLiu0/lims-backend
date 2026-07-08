package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.util.DBUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Data Access Object
 * Encapsulate template code and use reflection to automatically map ResultSet to Java POJO.
 */
public abstract class BaseJdbcDao {

    /**
     * 1.(INSERT / UPDATE / DELETE)
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
                        String columnName = metaData.getColumnLabel(i);
                        Object columnValue = rs.getObject(i);

                        if (columnValue != null) {
                        	// 2: Mapping database underscore fields to Java camelCase attributes
                            String fieldName = underscoreToCamelCase(columnName);
                            Field field = findField(clazz, fieldName);
                            if (field != null) {
                                field.setAccessible((true));
                                try {
                                    field.set(obj, columnValue);
                                } catch (IllegalArgumentException e) {
                                    System.err.println("[BaseJdbcDao] Type mistmatch for field '" + fieldName +
                                            "' on " + clazz.getSimpleName() + ": expected " + field.getType().getSimpleName() +
                                            " but got " + columnValue.getClass().getSimpleName());
                                }
                            } else {
                                System.err.println("[BaseJdbcDao] No matching field '" + fieldName + "' on " + clazz.getSimpleName());
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
                return current.getDeclaredField((fieldName));
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }
}