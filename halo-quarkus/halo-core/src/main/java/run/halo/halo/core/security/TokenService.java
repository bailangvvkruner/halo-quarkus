package run.halo.halo.core.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.jwt.Claims;
import run.halo.halo.core.entity.User;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class TokenService {
    
    private static final String ISSUER = "https://halo.quarkus.io";
    private static final Duration TOKEN_DURATION = Duration.ofHours(24);
    
    public String generateToken(User user) {
        Set<String> groups = new HashSet<>();
        groups.add(user.role.name());
        
        return Jwt.issuer(ISSUER)
                .upn(user.username)
                .groups(groups)
                .claim(Claims.sub.name(), user.id.toString())
                .claim(Claims.email.name(), user.email)
                .claim(Claims.full_name.name(), user.displayName)
                .claim(Claims.upn.name(), user.username)
                .expiresAt(Instant.now().plus(TOKEN_DURATION))
                .issuedAt(Instant.now())
                .sign();
    }
    
    public String generateRefreshToken(User user) {
        return Jwt.issuer(ISSUER)
                .upn(user.username)
                .claim(Claims.sub.name(), user.id.toString())
                .claim("type", "refresh")
                .expiresAt(Instant.now().plus(Duration.ofDays(30)))
                .sign();
    }
}
