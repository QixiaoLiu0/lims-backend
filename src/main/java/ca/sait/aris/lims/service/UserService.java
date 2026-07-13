package ca.sait.aris.lims.service;

import java.sql.Connection;

import ca.sait.aris.lims.dao.UserDAO;
import ca.sait.aris.lims.dto.req.LoginReqDTO;
import ca.sait.aris.lims.dto.req.ModifyPasswordReqDTO;
import ca.sait.aris.lims.dto.resp.AuthRespDTO;
import ca.sait.aris.lims.entity.User;
import ca.sait.aris.lims.util.DBUtil;
import ca.sait.aris.lims.util.TokenUtil;
import ca.sait.aris.lims.util.UserContext;

public class UserService {
	private final UserDAO userDao = new UserDAO();
	
	
	/**
     * handle user login
     */
    public AuthRespDTO login(LoginReqDTO req) throws Exception {
        try {
        	// 1. Trigger DBUtil binding connection to the current thread

        	// 2. Retrieve user entities based on email address

        	// 3. Password comparison (plaintext comparison is used in Sprint 2)

        	// 4. Generate JWT token

        	// 5. Assemble response DTO
        	
        	return null;
        } finally {
            // 6.Return HikariCP connection
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

            // 1. [Defense in Depth] Extract the current operator's ID and Email from the thread context

            // 2. Reuse `selectUserByEmail` to retrieve the user and perform secondary ID verification.

            // 3. Verify the old password is correct.

            // 4. Physically write the new password to db.

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
