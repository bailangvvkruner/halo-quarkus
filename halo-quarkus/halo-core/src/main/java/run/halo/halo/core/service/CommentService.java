package run.halo.halo.core.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import run.halo.halo.core.entity.Comment;
import run.halo.halo.core.repository.CommentRepository;

@ApplicationScoped
public class CommentService {
    
    @Inject
    CommentRepository commentRepository;
    
    public Uni<Comment> create(Comment comment) {
        return commentRepository.persist(comment);
    }
    
    public Uni<Void> delete(Long id) {
        return commentRepository.deleteById(id).replaceWithVoid();
    }
    
    public Uni<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }
    
    public Multi<Comment> findByTarget(Long targetId, Comment.TargetType targetType) {
        return commentRepository.findByTarget(targetId, targetType)
                .onItem().transformToMulti(comments -> Multi.createFrom().iterable(comments));
    }
}
