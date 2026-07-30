package ca.sait.aris.lims.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.util.TokenUtil;
import ca.sait.aris.lims.util.UserContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON Web Token (JWT) Authentication Filter.
 * * [Sprint 1 Notice]: To facilitate rapid feature delivery and unblock business logic testing,
 * this filter currently operates in a pass-through mode (unconditional forwarding).
 * Full cryptographic token verification and UserContext binding will be activated in Sprint 2.
 */
public class JwtAuthFilter implements Filter {
	// global route white list
	private final List<String> WHITELIST = Arrays.asList("/auth/login");
	
	//global permission matrix(URL Prefix -> Allowed Roles)
	private final Map<String, List<String>> PERMISSION_MATRIX = new HashMap<>();
	
	private Gson gson = new Gson();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	List<String> adminRoles = Arrays.asList("ADMIN", "SUPER_ADMIN");
    	List<String> superAdminOnly = Arrays.asList("SUPER_ADMIN");
//    	List<String> allUsers = Arrays.asList("STAFF", "ADMIN", "SUPER_ADMIN");
    	
    	
    	
    	//intercepts requests to /test-types, allowing only ADMIN and SUPER_ADMIN access.
    	PERMISSION_MATRIX.put("/test-types", adminRoles);
    	PERMISSION_MATRIX.put("/user", superAdminOnly);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
       
        // Get the request path (since the Servlet is mapped to /api/*, pathInfo here is similar to /auth/login or /test-types)
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "";
        }
        
        // --- 1. White list allows direct access ---
        if (isWhitelisted(pathInfo)) {
            chain.doFilter(req, res);
            return;
        }
        
        // --- 2. Extract and verify the token ---
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeErrorResponse(response, 401, "Unauthorized: Missing or invalid Authorization header.");
            return;
        }
        String token = authHeader.substring(7); // Strips the "Bearer" prefix
        DecodedJWT jwt = TokenUtil.verifyToken(token);
        if (jwt == null) {
            writeErrorResponse(response, 401, "Unauthorized: Token is invalid or expired.");
            return;
        }
        
        //parses token to get userId, email, role
        String userId = jwt.getClaim("userId").asString();
        String email = jwt.getClaim("email").asString();
        String role = jwt.getClaim("role").asString();
        
        // ---3. Verify whether the user's role has permission to access the route.---
        for (Map.Entry<String, List<String>> entry : PERMISSION_MATRIX.entrySet()) {
            if (pathInfo.startsWith(entry.getKey())) {
                List<String> allowedRoles = entry.getValue();
                if (!allowedRoles.contains(role)) {
                    writeErrorResponse(response, 403, "Forbidden: Insufficient privileges to access this resource.");
                    return;
                }
            }
        }
        
        // ---  4. Injects ThreadLocal Context ---
        try {
            UserContext.setUserId(userId);
            UserContext.setEmail(email);
            UserContext.setRole(role);
            
            // Allow the request to proceed to the downstream Servlet/Controller.
            chain.doFilter(req, res);
            
        } finally {
            //System rule: Context cleanup must be enforced in the finally block!
            UserContext.clear();
        }
    }

    
    private boolean isWhitelisted(String pathInfo) {
        return WHITELIST.stream().anyMatch(pathInfo::startsWith);
    }
    
    private void writeErrorResponse(HttpServletResponse response, int statusCode, String msg) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        
        // reuses Sprint 1 unified response body
        String json = gson.toJson(RespResult.error(statusCode, msg));
        response.getWriter().write(json);
    }
    
    @Override
    public void destroy() {}
}