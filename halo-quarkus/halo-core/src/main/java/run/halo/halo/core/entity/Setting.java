package run.halo.halo.core.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
public class Setting {
    
    @Id
    @Column(nullable = false, length = 100)
    public String key;
    
    @Column(columnDefinition = "TEXT")
    public String value;
    
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
}
