package ca.sait.aris.lims.entity;

import java.util.Date;

/**
 * Entity mapping strictly to database table 'user'.
 */
public class User {
    private String userId;
    private String createdByUserId;
    private String email;
    private String pwdHash;
    private String firstName;
    private String lastName;
    private String role;
    private Integer mustChangePassword;
    private Date createdAt;

    //0 parameters constructor
    public User() {}

    // Full parameters constructor
    public User(String userId, String createdByUserId, String email, String pwdHash, String firstName, String lastName, String role, Integer mustChangePassword, Date createdAt) {
        this.userId = userId;
        this.createdByUserId = createdByUserId;
        this.email = email;
        this.pwdHash = pwdHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.mustChangePassword = mustChangePassword;
        this.createdAt = createdAt;
    }

    
    
    // --- Getters & Setters ---
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public void setPwdHash(String pwdHash) {
        this.pwdHash = pwdHash;
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