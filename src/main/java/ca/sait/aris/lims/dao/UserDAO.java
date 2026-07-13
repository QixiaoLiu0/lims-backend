package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.entity.User;

/**
 * User Table Data Access Layer (DAO)
 * Inherits from BaseJdbcDao, directly calling the underlying template methods.
 */
public class UserDAO extends BaseJdbcDao {

    /**
     * 1. Retrieve the complete User entity object based on the unique email address
     * Used to extract password hashes and role information during login verification.
     */
    public User selectUserByEmail(String email) throws Exception {
        String sql = "SELECT user_id, created_by_user_id, email, pwd_hash, first_name, " +
                "last_name, role, must_change_password, created_at " +
                "FROM user " +
                "WHERE email = ?";

        return executeQueryForObject(sql, User.class, email);
    }

    /**
     * 2. Update user passwords and reset the forced password change flag to 0
     * For use by the password modification interface.
     */
    public void updatePassword(String userId, String newPasswordHash) throws Exception {
        String sql = "UPDATE user " +
                "SET pwd_hash = ?, must_change_password = 0 " +
                "WHERE user_id = ?";

        executeUpdate(sql, newPasswordHash, userId);
    }
}