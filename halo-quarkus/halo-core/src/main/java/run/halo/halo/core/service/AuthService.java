package run.halo.halo.core.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import run.halo.halo.core.dto.LoginRequest;
import run.halo.halo.core.dto.LoginResponse;
import run.halo.halo.core.dto.UserDTO;
import run.halo.halo.core.entity.User;
import run.halo.halo.core.repository.UserRepository;
import run.halo.halo.core.security.TokenService;

import java.util.logging.Logger;

@ApplicationScoped
public class AuthService {
    
    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    
    @Inject
    UserRepository userRepository;
    
    @Inject
    TokenService tokenService;
    
    @Inject
    UserService userService;
    
    public Uni<LoginResponse> login(LoginRequest request) {
        return userRepository.findByUsername(request.username)
                .onItem().ifNull().failWith(() -> new RuntimeException("User not found"))
                .chain(user -> {
                    if (!user.enabled) {
                        return Uni.createFrom().failure(new RuntimeException("User is disabled"));
                    }
                    return verifyPassword(user, request.password);
                })
                .chain(user -> {
                    String token = tokenService.generateToken(user);
                    String refreshToken = tokenService.generateRefreshToken(user);
                    UserDTO userDTO = UserDTO.fromEntity(user);
                    return Uni.createFrom().item(new LoginResponse(token, refreshToken, userDTO));
                });
    }
    
    private Uni<User> verifyPassword(User user, String rawPassword) {
        return Uni.createFrom().item(() -> {
            if (BCrypt.checkpw(rawPassword, user.password)) {
                return user;
            } else {
                throw new RuntimeException("Invalid password");
            }
        });
    }
    
    public Uni<UserDTO> getCurrentUser(String username) {
        return userService.findByUsername(username);
    }
}

class BCrypt {
    private static final int GENSALT_DEFAULT_LOG2_ROUNDS = 10;
    
    public static String hashpw(String password) {
        return org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt(GENSALT_DEFAULT_LOG2_ROUNDS));
    }
    
    public static boolean checkpw(String plaintext, String hashed) {
        return org.mindrot.jbcrypt.BCrypt.checkpw(plaintext, hashed);
    }
}
