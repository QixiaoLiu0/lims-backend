package ca.sait.aris.lims.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import ca.sait.aris.lims.util.DBUtil;



@WebListener
public class AppConfigListener implements ServletContextListener {
	 @Override
	    public void contextInitialized(ServletContextEvent sce) {
	        System.out.println("=================================================");
	        System.out.println("[AppConfigListener] Tomcat is starting. Initializing infrastructure...");
	        DBUtil.initPool();
	        System.out.println("=================================================");
	    }

	    @Override
	    public void contextDestroyed(ServletContextEvent sce) {
	        DBUtil.closePool();
	        System.out.println("[HikariCP] pool has been closed");
	        
	        // Manually unregister the underlying MySQL JDBC driver and stop its dedicated threads.
	        try {
	            // Iterate through and unregister all registered JDBC drivers.
	            Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
	            while (drivers.hasMoreElements()) {
	                Driver driver = drivers.nextElement();
	                DriverManager.deregisterDriver(driver);
	                System.out.println("[AppConfigListener] Deregistered JDBC driver: " + driver);
	            }
	            
	            // Stop the MySQL driver's built-in deprecated connection cleanup thread.
	            AbandonedConnectionCleanupThread.checkedShutdown();
	            System.out.println("[AppConfigListener] MySQL abandoned connection cleanup thread shut down.");
	            
	        } catch (Exception e) {
	            System.err.println("[AppConfigListener] Error during JDBC driver cleanup.");
	            e.printStackTrace();
	        }

	        System.out.println("[AppConfigListener] Tomcat is shutting down. Cleaning up resources ... ");
	    }
}
