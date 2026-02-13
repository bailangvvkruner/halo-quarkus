package run.halo.halo.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import run.halo.halo.core.entity.Post;

import java.time.LocalDateTime;
import java.util.Set;

public class PostDTO {
    public Long id;
    public String title;
    public String slug;
    public String content;
    public String excerpt;
    public String cover;
    public Long authorId;
    public Post.PostStatus status;
    public Boolean commentEnabled;
    public Integer topPriority;
    public Long views;
    public LocalDateTime publishTime;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    
    public static PostDTO fromEntity(Post post) {
        PostDTO dto = new PostDTO();
        dto.id = post.id;
        dto.title = post.title;
        dto.slug = post.slug;
        dto.content = post.content;
        dto.excerpt = post.excerpt;
        dto.cover = post.cover;
        dto.authorId = post.authorId;
        dto.status = post.status;
        dto.commentEnabled = post.commentEnabled;
        dto.topPriority = post.topPriority;
        dto.views = post.views;
        dto.publishTime = post.publishTime;
        dto.createdAt = post.createdAt;
        dto.updatedAt = post.updatedAt;
        return dto;
    }
}

public class CreatePostRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    public String title;
    
    @NotBlank(message = "Slug is required")
    @Size(max = 255, message = "Slug must be less than 255 characters")
    public String slug;
    
    @NotBlank(message = "Content is required")
    public String content;
    
    public String excerpt;
    public String cover;
    public Long authorId;
    public Post.PostStatus status = Post.PostStatus.DRAFT;
    public Boolean commentEnabled = true;
    public Integer topPriority = 0;
}

public class UpdatePostRequest {
    @Size(max = 255, message = "Title must be less than 255 characters")
    public String title;
    
    @Size(max = 255, message = "Slug must be less than 255 characters")
    public String slug;
    
    public String content;
    public String excerpt;
    public String cover;
    public Post.PostStatus status;
    public Boolean commentEnabled;
    public Integer topPriority;
}
