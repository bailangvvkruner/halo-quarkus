package run.halo.halo.core.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    
    @Column(nullable = false, unique = true, length = 50)
    public String username;
    
    @Column(nullable = false)
    public String password;
    
    @Column(length = 100)
    public String email;
    
    @Column(name = "display_name", length = 100)
    public String displayName;
    
    @Column
    public String avatar;
    
    @Column(columnDefinition = "TEXT")
    public String bio;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public Role role;
    
    @Column(nullable = false)
    public Boolean enabled = true;
    
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
    
    public enum Role {
        SUPER_ADMIN, ADMIN, CONTRIBUTOR, GUEST
    }
}
