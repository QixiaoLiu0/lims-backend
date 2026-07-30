package ca.sait.aris.lims.servlet;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.controller.AuthController;
import ca.sait.aris.lims.controller.CocController;
import ca.sait.aris.lims.controller.LookupController;
import ca.sait.aris.lims.controller.SampleController;
import ca.sait.aris.lims.controller.TestController;
import ca.sait.aris.lims.controller.TestTypeController;
import ca.sait.aris.lims.controller.UserController;
import ca.sait.aris.lims.dto.req.TestTypeSaveReqDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Technical Infrastructure Gateway (Front Controller).
 * [Strict Rule Compliance]: Configured ONLY for GET and POST HTTP methods.
 */
public class ApiGatewayServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    private TestTypeController testTypeController;
    private AuthController authController; // Sprint 2
    private LookupController lookupController;
    private CocController cocController;
    private SampleController sampleController;
    private TestController testController;
    private UserController userController;
    // other controllers
    
    private Gson gson;

    @Override
    public void init() throws ServletException {
       
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.gson = new GsonBuilder()
        	//not ignore null filed
        	.serializeNulls()
            // 1. Serialization (Backend -> Frontend): Convert LocalDateTime to a standard string.
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) 
                (src, typeOfSrc, context) -> new JsonPrimitive(src.format(formatter)))
            // 2. Deserialization (Frontend -> Backend): Converts strings to LocalDateTime and tolerates empty strings.
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) 
                (json, typeOfT, context) -> {
                    String datetime = json.getAsString();
                    if (datetime == null || datetime.trim().isEmpty()) {
                        return null;
                    }
                    return LocalDateTime.parse(datetime, formatter);
                })
            .create();
        
        this.testTypeController = new TestTypeController(this.gson);
        this.authController = new AuthController(this.gson);
        this.lookupController = new LookupController(this.gson);
        this.cocController = new CocController(this.gson);
        this.sampleController = new SampleController(this.gson);
        this.testController = new TestController(this.gson);
        this.userController = new UserController(this.gson);
        //instantiates other controllers
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        try {
            if (pathInfo == null) {
                writeJson(resp, RespResult.error(404, "Missing resource path."));
                return;
            }
            
            // --- Lookup Routing ---
            if (pathInfo.startsWith("/lookup")) {
                if ("/lookup/test-types".equals(pathInfo)) {
                    writeJson(resp, lookupController.getActiveTestTypes());
                    return;
                } else if ("/lookup/sample-types".equals(pathInfo)) {
                    writeJson(resp, lookupController.getSampleTypes());
                    return;
                }
            }
            
            // --- COC Routing ---
            if (pathInfo.startsWith("/cocs")) {
                String subPath = pathInfo.substring("/cocs".length());
                if (subPath.isEmpty() || "/".equals(subPath)) {
                    writeJson(resp, cocController.getDashboardCocs());
                } else {
                    String[] parts = subPath.split("/");
                    if (parts.length == 2) { // e.g., /cocs/{cocId} -> parts: ["", "{cocId}"]
                        writeJson(resp, cocController.getCocDetail(parts[1]));
                    } else {
                        writeJson(resp, RespResult.error(404, "Invalid COC GET route."));
                    }
                }
                return;
            }

            // --- Sample Routing ---
            if (pathInfo.startsWith("/samples")) {
                String subPath = pathInfo.substring("/samples".length());
                String[] parts = subPath.split("/");
                if (parts.length == 2) { // e.g., /samples/{sampleId}
                    writeJson(resp, sampleController.getSampleDetail(parts[1]));
                    return;
                }
            }

            // --- Test Routing ---
            if (pathInfo.startsWith("/tests")) {
                String subPath = pathInfo.substring("/tests".length());
                String[] parts = subPath.split("/");
                if (parts.length == 3 && "results".equals(parts[2])) { // e.g., /tests/{testId}/results
                    writeJson(resp, testController.getTestResults(parts[1]));
                    return;
                }
            }
            
            // --- Test Type Routing sprint 1---
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
            
            // --- Sample Routing ---
            if (pathInfo.startsWith("/samples")) {
                String subPath = pathInfo.substring("/samples".length());
                String[] parts = subPath.split("/");
                if (parts.length == 3) {
                    String sampleId = parts[1];
                    String action = parts[2];
                    if ("delete".equals(action)) {
                        writeJson(resp, sampleController.deleteSample(sampleId));
                    } else if ("tests".equals(action)) {
                        writeJson(resp, testController.appendTestToSample(sampleId, body));
                    } else {
                        writeJson(resp, RespResult.error(404, "Invalid Sample POST action."));
                    }
                    return;
                }
            }

            // --- Test Routing ---
            if (pathInfo.startsWith("/tests")) {
                String subPath = pathInfo.substring("/tests".length());
                String[] parts = subPath.split("/");
                if (parts.length == 3 && "delete".equals(parts[2])) {
                    writeJson(resp, testController.deleteTest(parts[1]));
                    return;
                } else if (parts.length == 4 && "results".equals(parts[2]) && "save".equals(parts[3])) {
                    writeJson(resp, testController.saveTestResults(parts[1], body));
                    return;
                }
            }
            
            //other APIs routes...
            

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