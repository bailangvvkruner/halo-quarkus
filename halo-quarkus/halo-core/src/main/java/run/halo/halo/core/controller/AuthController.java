package run.halo.halo.core.controller;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import run.halo.halo.core.dto.LoginRequest;
import run.halo.halo.core.dto.LoginResponse;
import run.halo.halo.core.dto.UserDTO;
import run.halo.halo.core.entity.User;
import run.halo.halo.core.repository.UserRepository;
import run.halo.halo.core.service.AuthService;
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
    JsonWebToken jwt;
    
    @POST
    @Path("/login")
    public Uni<Response> login(LoginRequest request) {
        return authService.login(request)
                .map(response -> Response.ok(response).build())
                .onFailure().recoverWithItem(throwable -> 
                    Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"" + throwable.getMessage() + "\"}")
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
        return Response.ok().entity("{\"message\":\"Logged out successfully\"}").build();
    }
}
