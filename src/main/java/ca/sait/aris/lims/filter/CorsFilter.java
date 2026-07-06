package ca.sait.aris.lims.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Global Cross-Origin Resource Sharing (CORS) Filter.
 * Acts as the first line of defense to handle browser preflight OPTIONS requests.
 */
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // 1. Inject mandatory CORS headers for Next.js frontend direct connectivity
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // 2. CRITICAL PREFLIGHT INTERCEPTION:
        // If the request method is OPTIONS, bypass the remaining filters/servlets and return 200 OK immediately.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return; // Terminate the filter chain execution for preflight check
        }

        // 3. For standard operational requests (GET, POST, etc.), forward to the next element in the pipeline
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}
