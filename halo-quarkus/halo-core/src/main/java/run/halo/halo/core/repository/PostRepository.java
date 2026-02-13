package run.halo.halo.core.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import run.halo.halo.core.entity.Post;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
    
    public Uni<Post> findBySlug(String slug) {
        return find("slug", slug).firstResult();
    }
    
    public Uni<Boolean> existsBySlug(String slug) {
        return count("slug", slug).map(count -> count > 0);
    }
}
