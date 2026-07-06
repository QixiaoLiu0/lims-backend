package ca.sait.aris.lims.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
	        System.out.println("[AppConfigListener] Tomcat is shutting down. Cleaning up resources...");
	    }
}
