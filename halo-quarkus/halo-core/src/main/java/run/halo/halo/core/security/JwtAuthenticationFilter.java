package run.halo.halo.core.security;

import io.smallrye.jwt.auth.principal.JWTParser;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.jwt.config.JwtConfig;

import java.util.logging.Logger;

@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class JwtAuthenticationFilter implements ContainerRequestFilter {
    
    private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class.getName());
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Inject
    JWTParser parser;
    
    @Inject
    JwtConfig jwtConfig;
    
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return;
        }
        
        try {
            String token = authHeader.substring(BEARER_PREFIX.length());
            JsonWebToken jwt = parser.parse(token);
            
            if (jwt == null || !jwtConfig.verify(jwt)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Invalid or expired token\"}")
                        .build());
                return;
            }
            
            requestContext.setProperty("jwt", jwt);
            
        } catch (Exception e) {
            LOGGER.warning("JWT authentication failed: " + e.getMessage());
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Authentication failed\"}")
                    .build());
        }
    }
}
