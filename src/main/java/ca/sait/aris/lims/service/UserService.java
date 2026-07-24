package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.UserDAO;
import ca.sait.aris.lims.dto.req.LoginReqDTO;
import ca.sait.aris.lims.dto.req.ModifyPasswordReqDTO;
import ca.sait.aris.lims.dto.req.NewUserReqDTO;
import ca.sait.aris.lims.dto.req.UpdateRoleReqDTO;
import ca.sait.aris.lims.dto.resp.AuthRespDTO;
import ca.sait.aris.lims.dto.resp.UserRespDTO;
import ca.sait.aris.lims.entity.User;
import ca.sait.aris.lims.util.DBUtil;
import ca.sait.aris.lims.util.TokenUtil;
import ca.sait.aris.lims.util.UserContext;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserService {

    private final UserDAO userDao = new UserDAO();

    /**
     * handles user login
     */
    public AuthRespDTO login(LoginReqDTO req) throws Exception {
        try {
            // 1. DBUtil binding connection to the current thread
            DBUtil.getConnection();

            // 2. Retrieve user entities based on email address
            User user = userDao.selectUserByEmail(req.getEmail());
            if (user == null) {
                throw new Exception("Authentication failed: Invalid email or password.");
            }

            // 3. Password comparison (plaintext comparison is used in Sprint 2)
            if (!user.getPwdHash().equals(req.getPassword())) {
                throw new Exception("Authentication failed: Invalid email or password.");
            }

            // 4. Generate JWT token
            String token = TokenUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole());

            // 5.  Assemble response DTO
            AuthRespDTO resp = new AuthRespDTO();
            resp.setToken(token);
            resp.setUserId(user.getUserId());
            resp.setEmail(user.getEmail());
            resp.setRole(user.getRole());

            return resp;

        } finally {
            // 6.Return HikariCP connection
            DBUtil.closeConnection();
        }
    }

    /**
     * Handles password modify
     */
    public void modifyPassword(ModifyPasswordReqDTO req) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // opens transaction

            // 1. Extract the current operator's ID and Email from the thread context
            String contextUserId = UserContext.getUserId();
            String contextEmail = UserContext.getEmail();

            if (contextUserId == null || contextEmail == null) {
                throw new Exception("Security Violation: Missing user context.");
            }

            // 2. Reuse `selectUserByEmail` to retrieve the user and perform secondary ID verification.
            User user = userDao.selectUserByEmail(contextEmail);
            if (user == null || !user.getUserId().equals(contextUserId)) {
                throw new Exception("Security Violation: User identity mismatch.");
            }

            // 3. Verify the old password is correct.
            if (!user.getPwdHash().equals(req.getOldPassword())) {
                throw new Exception("Modification failed: Incorrect old password.");
            }

            // 4. Physically write the new password to db.
            userDao.updatePassword(contextUserId, req.getNewPassword());

            // 5. Commit the transaction.
            conn.commit();

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            DBUtil.closeConnection();
        }
    }
    
    
    
    /**
     * Get all users
     */
	public List<UserRespDTO> getAllUsers() throws Exception{
		Connection conn = null;
	    try {
	        conn = DBUtil.getConnection();
	        List<User> users = userDao.selectAllUsers();
	        
	     
	        List<UserRespDTO> dtoList = new ArrayList<>();
	        
	        for (User user : users) {
	            UserRespDTO dto = new UserRespDTO();
	            dto.setUserId(user.getUserId());
	            dto.setEmail(user.getEmail());
	            dto.setFirstName(user.getFirstName());
	            dto.setLastName(user.getLastName());
	            dto.setRole(user.getRole());
	            dto.setMustChangePassword(user.getMustChangePassword());
	            dto.setCreatedAt(user.getCreatedAt());
	            
	            dtoList.add(dto);
	        }
	        
	        return dtoList;
	    } finally {
	        DBUtil.closeConnection();
	    }
	}

	public void createUser(NewUserReqDTO req) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // Extract the creator's ID from thread context
            String contextUserId = UserContext.getUserId();
            if (contextUserId == null) {
                throw new Exception("Security Violation: Missing user context.");
            }

            // Construct new user
            User newUser = new User();
            newUser.setUserId(UUID.randomUUID().toString());
            newUser.setCreatedByUserId(contextUserId);
            newUser.setEmail(req.getEmail());
            // Note: BCrypt 
            newUser.setPwdHash(req.getPassword()); 
            newUser.setFirstName(req.getFirstName());
            newUser.setLastName(req.getLastName());
            newUser.setRole(req.getRole());
            newUser.setMustChangePassword(1);
            newUser.setCreatedAt(new Date());

            userDao.insertUser(newUser);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            DBUtil.closeConnection();
        }
    }

	public void updateUserRole(String targetUserId, UpdateRoleReqDTO req) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            String contextUserId = UserContext.getUserId();
            if (contextUserId == null) {
                throw new Exception("Security Violation: Missing user context.");
            }

            userDao.updateUserRole(targetUserId, req.getRole());

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            DBUtil.closeConnection();
        }
    }
}