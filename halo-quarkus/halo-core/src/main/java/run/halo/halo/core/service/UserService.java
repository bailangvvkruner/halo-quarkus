package run.halo.halo.core.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import run.halo.halo.core.dto.UserDTO;
import run.halo.halo.core.entity.User;
import run.halo.halo.core.repository.UserRepository;

@ApplicationScoped
public class UserService {
    
    @Inject
    UserRepository userRepository;
    
    public Uni<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserDTO::fromEntity);
    }
    
    public Uni<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(UserDTO::fromEntity);
    }
    
    public Uni<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDTO::fromEntity);
    }
    
    public Uni<Boolean> existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public Uni<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
