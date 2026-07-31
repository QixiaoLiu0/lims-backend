package ca.sait.aris.lims.dto.resp;

import java.util.Date;


public class UserRespDTO {
	private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Integer mustChangePassword;
    private Date createdAt;
    
    public UserRespDTO() {}
    
	public UserRespDTO(String userId, String email, String firstName, String lastName, String role,
			Integer mustChangePassword, Date createdAt) {
		super();
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.mustChangePassword = mustChangePassword;
		this.createdAt = createdAt;
	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getMustChangePassword() {
		return mustChangePassword;
	}
	public void setMustChangePassword(Integer mustChangePassword) {
		this.mustChangePassword = mustChangePassword;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
