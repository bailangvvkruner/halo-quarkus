package run.halo.halo.core.security;

import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Provider
@Priority(Priorities.AUTHORIZATION)
@ApplicationScoped
public class RoleAuthorizationFilter implements ContainerRequestFilter {
    
    private static final Logger LOGGER = Logger.getLogger(RoleAuthorizationFilter.class.getName());
    
    @Override
    public void filter(ContainerRequestContext requestContext) {
        Object jwtObject = requestContext.getProperty("jwt");
        
        if (jwtObject == null) {
            return;
        }
        
        JsonWebToken jwt = (JsonWebToken) jwtObject;
        Set<String> userRoles = new HashSet<>(jwt.getGroups());
        
        RolesAllowed rolesAllowed = getResourceClassRolesAllowed(requestContext);
        
        if (rolesAllowed != null) {
            Set<String> allowedRoles = new HashSet<>(Arrays.asList(rolesAllowed.value()));
            
            if (!hasAnyRole(userRoles, allowedRoles)) {
                LOGGER.warning("Access denied. User roles: " + userRoles + ", Required: " + allowedRoles);
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\":\"Access denied\"}")
                        .build());
            }
        }
    }
    
    private boolean hasAnyRole(Set<String> userRoles, Set<String> allowedRoles) {
        if (userRoles.contains("SUPER_ADMIN")) {
            return true;
        }
        
        for (String allowed : allowedRoles) {
            if (userRoles.contains(allowed)) {
                return true;
            }
        }
        return false;
    }
    
    private RolesAllowed getResourceClassRolesAllowed(ContainerRequestContext requestContext) {
        return requestContext.getUriInfo()
                .getMatchedResourceMethod()
                .getAnnotation(RolesAllowed.class);
    }
}
