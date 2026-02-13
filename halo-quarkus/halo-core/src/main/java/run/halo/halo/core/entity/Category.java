package run.halo.halo.core.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category extends PanacheEntity {
    
    @Column(nullable = false, unique = true, length = 100)
    public String name;
    
    @Column(nullable = false, unique = true, length = 100)
    public String slug;
    
    @Column(columnDefinition = "TEXT")
    public String description;
    
    @Column
    public String cover;
    
    @Column
    public Integer priority = 0;
    
    @Column(name = "post_count")
    public Integer postCount = 0;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
    
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    public Set<Post> posts = new HashSet<>();
    
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
