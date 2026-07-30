package ca.sait.aris.lims.controller;

import java.util.List;

import com.google.gson.Gson;

import ca.sait.aris.lims.common.RespResult;
import ca.sait.aris.lims.dto.req.NewUserReqDTO;
import ca.sait.aris.lims.dto.req.UpdateRoleReqDTO;
import ca.sait.aris.lims.dto.resp.UserRespDTO;
import ca.sait.aris.lims.service.UserService;

public class UserController {
	private final UserService userService = new UserService();
	private final Gson gson;
	
	public UserController(Gson gson){
    	this.gson = gson;
    }
	
	

	// API 14: Get All Users
    public RespResult<List<UserRespDTO>> getAllUsers() {
        try {
            List<UserRespDTO> users = userService.getAllUsers();
            return RespResult.success(users);
        } catch (Exception e) {
            return RespResult.error(500, e.getMessage());
        }
    }

    // API 15: New A User
    public RespResult<String> createUser(String jsonBody) {
        try {
            NewUserReqDTO req = gson.fromJson(jsonBody, NewUserReqDTO.class);
            if (req == null || req.getEmail() == null || req.getRole() == null) {
                return RespResult.error(400, "Validation Failed: Email and role are required.");
            }
            userService.createUser(req);
            return RespResult.success();
        } catch (Exception e) {
            return RespResult.error(500, e.getMessage());
        }
    }

    // API 16: Update User's Role
    public RespResult<String> updateUserRole(String targetUserId, String jsonBody) {
        try {
            UpdateRoleReqDTO req = gson.fromJson(jsonBody, UpdateRoleReqDTO.class);
            if (req == null || req.getRole() == null) {
                return RespResult.error(400, "Validation Failed: Target role is required.");
            }
            userService.updateUserRole(targetUserId, req);
            return RespResult.success();
        } catch (Exception e) {
            return RespResult.error(500, e.getMessage());
        }
    }
}
