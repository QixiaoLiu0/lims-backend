package ca.sait.aris.lims.service;

import ca.sait.aris.lims.dao.UserDAO;
import ca.sait.aris.lims.dto.req.LoginReqDTO;
import ca.sait.aris.lims.dto.req.ModifyPasswordReqDTO;
import ca.sait.aris.lims.dto.resp.AuthRespDTO;
import ca.sait.aris.lims.entity.User;
import ca.sait.aris.lims.util.DBUtil;
import ca.sait.aris.lims.util.TokenUtil;
import ca.sait.aris.lims.util.UserContext;

import java.sql.Connection;

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
}