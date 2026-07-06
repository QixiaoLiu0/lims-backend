package ca.sait.aris.lims.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static HikariDataSource dataSource;
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();

    /**
     * Read external configuration and initialize HikariCP connection pool
     */
    public static void initPool() {
        try {
            Properties props = new Properties();
            // Load database configuration from classpath (src/main/resources)
            try (InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is == null) {
                    throw new RuntimeException("db.properties file not found in classpath.");
                }
                props.load(is);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            config.setDriverClassName(props.getProperty("db.driverClassName"));

            // config optimization
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.maximumPoolSize", "5")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("db.minimumIdle", "1")));
            config.setIdleTimeout(Long.parseLong(props.getProperty("db.idleTimeout", "30000")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("db.connectionTimeout", "2000")));

            dataSource = new HikariDataSource(config);
            System.out.println("[DBUtil] HikariCP Connection Pool initialized successfully.");
        } catch (Exception e) {
            System.err.println("[DBUtil] Catastrophic error: Failed to initialize HikariCP connection pool.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = CONNECTION_HOLDER.get();

        // If the current thread does not have a bound connection, or the connection has been closed unexpectedly, a new connection is obtained from the pool.
        if (conn == null || conn.isClosed()) {
            if (dataSource == null) {
                throw new SQLException("Database connection pool has not been initialized.");
            }
            conn = dataSource.getConnection();
            CONNECTION_HOLDER.set(conn); // Bind to the current thread
        }
        return conn;
    }

    public static void closeConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            try {
                conn.close(); // return it to the HikariCP pool.
            } catch (SQLException e) {
                System.err.println("[DBUtil] Error returning connection to pool: " + e.getMessage());
            } finally {
                CONNECTION_HOLDER.remove(); // Prevent Tomcat thread pool memory leaks
            }
        }
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("[DBUtil] HikariCP Connection Pool closed.");
        }
    }
}