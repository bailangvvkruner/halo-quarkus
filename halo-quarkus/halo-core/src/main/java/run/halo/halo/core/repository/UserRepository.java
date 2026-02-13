package run.halo.halo.core.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import run.halo.halo.core.entity.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
    public Uni<User> findByUsername(String username) {
        return find("username", username).firstResult();
    }
    
    public Uni<User> findByEmail(String email) {
        return find("email", email).firstResult();
    }
    
    public Uni<Boolean> existsByUsername(String username) {
        return count("username", username).map(count -> count > 0);
    }
    
    public Uni<Boolean> existsByEmail(String email) {
        return count("email", email).map(count -> count > 0);
    }
}
