package ca.sait.aris.lims.dto.req;

public class LoginReqDTO {
    private String email;
    private String password;

    public LoginReqDTO() {}

    
    
    
    
    //setters  & getters
    public String getEmail() {return email;}
    public void setEmail(String email) { this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}