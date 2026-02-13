package run.halo.halo.core.dto;

import run.halo.halo.core.entity.User;

public class UserDTO {
    public Long id;
    public String username;
    public String email;
    public String displayName;
    public String avatar;
    public String bio;
    public User.Role role;
    public Boolean enabled;
    
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.id;
        dto.username = user.username;
        dto.email = user.email;
        dto.displayName = user.displayName;
        dto.avatar = user.avatar;
        dto.bio = user.bio;
        dto.role = user.role;
        dto.enabled = user.enabled;
        return dto;
    }
}
