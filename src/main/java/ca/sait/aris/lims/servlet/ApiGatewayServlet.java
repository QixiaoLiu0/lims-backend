package ca.sait.aris.lims.servlet;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.controller.AuthController;
import ca.sait.aris.lims.controller.TestTypeController;
import ca.sait.aris.lims.dto.req.TestTypeSaveReqDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Technical Infrastructure Gateway (Front Controller).
 * [Strict Rule Compliance]: Configured ONLY for GET and POST HTTP methods.
 */
public class ApiGatewayServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    private TestTypeController testTypeController;
    private AuthController authController; // Sprint 2
    // other controllers
    
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.testTypeController = new TestTypeController();
        this.authController = new AuthController();
        //instantiates other controllers
        
        this.gson = new GsonBuilder().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        try {
            if (pathInfo == null) {
                writeJson(resp, RespResult.error(404, "Missing resource path."));
                return;
            }

            if (pathInfo.startsWith("/test-types")) {
                String subPath = pathInfo.substring("/test-types".length());
                
                if (subPath.isEmpty() || "/".equals(subPath)) {
                    // 2：GET /api/test-types (Get Test Type List)
                    writeJson(resp, testTypeController.getTestTypeList());
                } else {
                    // 3：GET /api/test-types/{id} (Get Test Type Detail)
                    int id = Integer.parseInt(subPath.replace("/", ""));
                    writeJson(resp, testTypeController.getTestTypeDetail(id));
                }
                return;
            }

            writeJson(resp, RespResult.error(404, "API endpoint not found."));
        } catch (NumberFormatException e) {
            writeJson(resp, RespResult.error(400, "Invalid resource identifier format."));
        } catch (Exception e) {
            writeJson(resp, RespResult.error(500, "Internal Server Error: " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        try {
            if (pathInfo == null) {
                writeJson(resp, RespResult.error(404, "Missing resource path."));
                return;
            }

            String body = readRequestBody(req);
            
            
            
            // --- Auth Routing (Sprint 2) ---
            if (pathInfo.startsWith("/auth")) {
                if ("/auth/login".equals(pathInfo)) {
                    // POST /api/auth/login
                    writeJson(resp, authController.handleLogin(body));
                    return;
                } else if ("/auth/password".equals(pathInfo)) {
                    // POST /api/auth/password
                    writeJson(resp, authController.handleModifyPassword(body));
                    return;
                }
            }
            
            
            
            
            // --- Test Type Routing (Sprint 1)  ---
            if (pathInfo.startsWith("/test-types")) {
                String subPath = pathInfo.substring("/test-types".length());

                // 1：POST /api/test-types (Create Test Type)
                if (subPath.isEmpty() || "/".equals(subPath)) {
                    TestTypeSaveReqDTO reqDto = gson.fromJson(body, TestTypeSaveReqDTO.class);
                    writeJson(resp, testTypeController.createTestType(reqDto));
                    return;
                } 
                // 4：POST /api/test-types/{id} (Update Test Type)
                else {
                    int id = Integer.parseInt(subPath.replace("/", ""));
                    TestTypeSaveReqDTO reqDto = gson.fromJson(body, TestTypeSaveReqDTO.class);
                    writeJson(resp, testTypeController.updateTestType(id, reqDto));
                    return;
                }
            }

            writeJson(resp, RespResult.error(404, "API endpoint not found."));
        } catch (NumberFormatException e) {
            writeJson(resp, RespResult.error(400, "Invalid resource identifier format."));
        } catch (Exception e) {
            writeJson(resp, RespResult.error(500, "Internal Server Error: " + e.getMessage()));
        }
    }

    // --- Technical Helpers ---
    
    private String readRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private void writeJson(HttpServletResponse resp, RespResult<?> result) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }
}