package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.entity.User;

/**
 * User Table Data Access Layer (DAO)
 * Inherits from BaseJdbcDao, directly calling the underlying template methods.
 */
public class UserDAO extends BaseJdbcDao{
	/**
     * 1. Retrieve the complete User entity object based on the unique email address
	 * Used to extract password hashes and role information during login verification.
     */
    public User selectUserByEmail(String email) throws Exception {
    	//TODO
        return null;
    }

    /**
     * 2. Update user passwords and reset the forced password change flag to 0
     * For use by the password change interface.
     */
    public void updatePassword(String userId, String newPasswordHash) throws Exception {
        //TODO
    }
}
