package run.halo.halo.core.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import run.halo.halo.core.entity.Comment;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {
    
    public Uni<java.util.List<Comment>> findByTarget(Long targetId, Comment.TargetType targetType) {
        return list("targetId = ?1 and targetType = ?2", targetId, targetType);
    }
}
