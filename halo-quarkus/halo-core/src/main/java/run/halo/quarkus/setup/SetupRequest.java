package run.halo.quarkus.setup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SetupRequest {
    
    @NotBlank
    @Size(min = 4, max = 63)
    @Pattern(regexp = "^[a-z0-9]([-a-z0-9]*[a-z0-9])?$", message = "Username must match pattern")
    private String username;
    
    @NotBlank
    @Size(min = 5, max = 257)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain uppercase, lowercase, and numbers")
    private String password;
    
    @Email
    private String email;
    
    @NotBlank
    @Size(max = 80)
    private String siteTitle;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSiteTitle() {
        return siteTitle;
    }
    
    public void setSiteTitle(String siteTitle) {
        this.siteTitle = siteTitle;
    }
}
