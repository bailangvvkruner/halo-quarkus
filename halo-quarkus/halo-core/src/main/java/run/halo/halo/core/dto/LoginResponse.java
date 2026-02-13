package run.halo.halo.core.dto;

public class LoginResponse {
    public String token;
    public String refreshToken;
    public UserDTO user;
    
    public LoginResponse(String token, String refreshToken, UserDTO user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
    }
}
