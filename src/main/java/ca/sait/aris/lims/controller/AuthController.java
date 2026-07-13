package ca.sait.aris.lims.controller;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.req.LoginReqDTO;
import ca.sait.aris.lims.dto.req.ModifyPasswordReqDTO;
import ca.sait.aris.lims.dto.resp.AuthRespDTO;
import ca.sait.aris.lims.service.UserService;
import com.google.gson.Gson;

/**
 * 面向网关的认证业务终点站
 */
public class AuthController {

    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    /**
     * route: POST /api/auth/login
     */
    public RespResult<AuthRespDTO> handleLogin(String jsonBody) {
        try {
            LoginReqDTO req = gson.fromJson(jsonBody, LoginReqDTO.class);
            
            if (req == null || req.getEmail() == null || req.getPassword() == null) {
                return RespResult.error(400, "Validation Failed: Email and password are required.");
            }

            AuthRespDTO authRespDTO = userService.login(req);
            
            return RespResult.success(authRespDTO);

        } catch (Exception e) {
            return RespResult.error(401, e.getMessage());
        }
    }

    /**
     * 路由: POST /api/auth/password
     */
    public RespResult<Object> handleModifyPassword(String jsonBody) {
        try {
            ModifyPasswordReqDTO req = gson.fromJson(jsonBody, ModifyPasswordReqDTO.class);
            
            if (req == null || req.getOldPassword() == null || req.getNewPassword() == null) {
                return RespResult.error(400, "Validation Failed: Old and new passwords are required.");
            }

            userService.modifyPassword(req);

            RespResult<Object> result = RespResult.success();
            result.setMessage("Password updated successfully. Please log in again with your new credentials.");
            return result;

        } catch (Exception e) {
            return RespResult.error(400, e.getMessage());
        }
    }
}