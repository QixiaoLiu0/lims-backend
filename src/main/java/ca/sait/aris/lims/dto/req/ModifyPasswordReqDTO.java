package ca.sait.aris.lims.dto.req;

/**
 * payload of modifying password request 
 *  warning: UserId must not be included here; it must be retrieved from the ThreadLocal context by the backend to prevent unauthorized modification.
 */
public class ModifyPasswordReqDTO {
    private String oldPassword;
    private String newPassword;

    public ModifyPasswordReqDTO() {}

    
    
    
    
    // setters & getters
    public String getOldPassword() {return oldPassword;}

    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword;}

    public String getNewPassword() { return newPassword;}

    public void setNewPassword(String newPassword) { this.newPassword = newPassword;}
}