package run.halo.halo.core.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post extends PanacheEntity {
    
    @Column(nullable = false)
    public String title;
    
    @Column(nullable = false, unique = true)
    public String slug;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    public String content;
    
    @Column(columnDefinition = "TEXT")
    public String excerpt;
    
    @Column
    public String cover;
    
    @Column(name = "author_id", nullable = false)
    public Long authorId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public PostStatus status;
    
    @Column(name = "comment_enabled")
    public Boolean commentEnabled = true;
    
    @Column(name = "top_priority")
    public Integer topPriority = 0;
    
    @Column
    public Long views = 0L;
    
    @Column(name = "publish_time")
    public LocalDateTime publishTime;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "post_category",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    public Set<Category> categories = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    public Set<Tag> tags = new HashSet<>();
    
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == PostStatus.PUBLISHED && publishTime == null) {
            publishTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status == PostStatus.PUBLISHED && publishTime == null) {
            publishTime = LocalDateTime.now();
        }
    }
    
    public enum PostStatus {
        DRAFT, PUBLISHED, INTRODUCE_ONLY, RECYCLE
    }
}
