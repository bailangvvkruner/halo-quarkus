package run.halo.halo.core.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import run.halo.halo.core.dto.PostDTO;
import run.halo.halo.core.entity.Post;
import run.halo.halo.core.repository.PostRepository;

@ApplicationScoped
public class PostService {
    
    @Inject
    PostRepository postRepository;
    
    public Uni<PostDTO> create(Post post) {
        return postRepository.persist(post)
                .replaceWith(() -> PostDTO.fromEntity(post));
    }
    
    public Uni<PostDTO> update(Long id, Post post) {
        return postRepository.findById(id)
                .onItem().ifNull().failWith(() -> new RuntimeException("Post not found"))
                .chain(existingPost -> {
                    existingPost.title = post.title;
                    existingPost.slug = post.slug;
                    existingPost.content = post.content;
                    existingPost.excerpt = post.excerpt;
                    existingPost.cover = post.cover;
                    existingPost.status = post.status;
                    existingPost.commentEnabled = post.commentEnabled;
                    existingPost.topPriority = post.topPriority;
                    return postRepository.persist(existingPost);
                })
                .map(updatedPost -> PostDTO.fromEntity(updatedPost));
    }
    
    public Uni<Void> delete(Long id) {
        return postRepository.deleteById(id)
                .replaceWithVoid();
    }
    
    public Uni<PostDTO> findById(Long id) {
        return postRepository.findById(id)
                .map(PostDTO::fromEntity);
    }
    
    public Uni<PostDTO> findBySlug(String slug) {
        return postRepository.findBySlug(slug)
                .map(PostDTO::fromEntity);
    }
    
    public Multi<PostDTO> findAll() {
        return postRepository.listAll()
                .map(posts -> posts.stream()
                        .map(PostDTO::fromEntity)
                        .toList())
                .onItem().transformToMulti(list -> Multi.createFrom().iterable(list));
    }
    
    public Multi<PostDTO> findPublished() {
        return postRepository.list("status", Post.PostStatus.PUBLISHED)
                .map(posts -> posts.stream()
                        .map(PostDTO::fromEntity)
                        .toList())
                .onItem().transformToMulti(list -> Multi.createFrom().iterable(list));
    }
}
