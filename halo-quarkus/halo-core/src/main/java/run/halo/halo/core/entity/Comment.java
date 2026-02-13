package run.halo.halo.core.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment extends PanacheEntity {
    
    @Column(nullable = false, columnDefinition = "TEXT")
    public String content;
    
    @Column(nullable = false, length = 100)
    public String author;
    
    @Column(length = 100)
    public String email;
    
    @Column
    public String website;
    
    @Column
    public String ip;
    
    @Column(name = "user_agent")
    public String userAgent;
    
    @Column(name = "owner_id")
    public Long ownerId;
    
    @Column(name = "target_id", nullable = false)
    public Long targetId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    public TargetType targetType;
    
    @Column(name = "parent_id")
    public Long parentId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public CommentStatus status;
    
    @Column(name = "top_priority")
    public Integer topPriority = 0;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
    
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum TargetType {
        POST, SINGLE_PAGE
    }
    
    public enum CommentStatus {
        PUBLISHED, AUDITING, RECYCLE
    }
}
