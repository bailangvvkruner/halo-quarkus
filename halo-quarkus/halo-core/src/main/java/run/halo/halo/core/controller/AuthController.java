package run.halo.halo.core.controller;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import run.halo.halo.core.dto.InstallRequest;
import run.halo.halo.core.dto.LoginRequest;
import run.halo.halo.core.dto.LoginResponse;
import run.halo.halo.core.dto.UserDTO;
import run.halo.halo.core.entity.Setting;
import run.halo.halo.core.entity.User;
import run.halo.halo.core.repository.UserRepository;
import run.halo.halo.core.service.AuthService;
import run.halo.halo.core.service.SettingService;
import run.halo.halo.core.service.UserService;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    
    @Inject
    AuthService authService;
    
    @Inject
    UserService userService;
    
    @Inject
    UserRepository userRepository;
    
    @Inject
    SettingService settingService;
    
    @Inject
    JsonWebToken jwt;
    
    @GET
    @Path("/is-installed")
    public Uni<Response> isInstalled() {
        return settingService.isInstalled()
                .map(installed -> Response.ok(Map.of("installed", installed)).build());
    }
    
    @POST
    @Path("/install")
    public Uni<Response> install(InstallRequest request) {
        return settingService.isInstalled()
                .chain(installed -> {
                    if (installed) {
                        return Uni.createFrom().failure(new RuntimeException("System already installed"));
                    }
                    return createAdminUser(request);
                })
                .chain(user -> settingService.markAsInstalled()
                        .replaceWith(user))
                .map(user -> Response.ok(UserDTO.fromEntity(user)).build())
                .onFailure().recoverWithItem(throwable -> 
                    Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", throwable.getMessage()))
                        .build()
                );
    }
    
    private Uni<User> createAdminUser(InstallRequest request) {
        return settingService.isInstalled()
                .chain(installed -> {
                    if (installed) {
                        return Uni.createFrom().failure(new RuntimeException("Already installed"));
                    }
                    User user = new User();
                    user.username = request.username;
                    user.password = BCrypt.hashpw(request.password);
                    user.email = request.email;
                    user.displayName = request.username;
                    user.role = User.Role.SUPER_ADMIN;
                    user.enabled = true;
                    return userRepository.persist(user);
                });
    }
    
    @POST
    @Path("/login")
    public Uni<Response> login(LoginRequest request) {
        return settingService.isInstalled()
                .chain(installed -> {
                    if (!installed) {
                        return Uni.createFrom().failure(new RuntimeException("System not installed"));
                    }
                    return authService.login(request);
                })
                .map(response -> Response.ok(response).build())
                .onFailure().recoverWithItem(throwable -> 
                    Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("error", throwable.getMessage()))
                        .build()
                );
    }
    
    @GET
    @Path("/me")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "CONTRIBUTOR", "GUEST"})
    public Uni<Response> getCurrentUser() {
        String username = jwt.getName();
        return userService.findByUsername(username)
                .map(user -> Response.ok(user).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.UNAUTHORIZED).build());
    }
    
    @POST
    @Path("/logout")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "CONTRIBUTOR", "GUEST"})
    public Response logout() {
        return Response.ok().entity(Map.of("message", "Logged out successfully")).build();
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
