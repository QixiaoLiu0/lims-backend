package ca.sait.aris.lims.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

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
    
    
    public List<User> selectAllUsers() throws Exception{
		String sql = "SELECT user_id, created_by_user_id, email, pwd_hash, first_name, last_name, " +
                "role, must_change_password, created_at FROM user";
		return executeQuery(sql, User.class);
	}

	public void insertUser(User user) throws SQLException {
        String sql = "INSERT INTO user (user_id, created_by_user_id, email, pwd_hash, first_name, " +
                     "last_name, role, must_change_password, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        executeUpdate(sql, 
            user.getUserId(), 
            user.getCreatedByUserId(), 
            user.getEmail(), 
            user.getPwdHash(), 
            user.getFirstName(), 
            user.getLastName(), 
            user.getRole(), 
            user.getMustChangePassword(), 
            new Timestamp(user.getCreatedAt().getTime())
        );
    }

	public void updateUserRole(String userId, String role) throws Exception {
        String sql = "UPDATE user SET role = ? WHERE user_id = ?";
        executeUpdate(sql, role, userId);
    }
}