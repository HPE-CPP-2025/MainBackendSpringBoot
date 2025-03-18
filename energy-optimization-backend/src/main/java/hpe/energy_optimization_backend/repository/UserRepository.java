package hpe.energy_optimization_backend.repository;

import hpe.energy_optimization_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findByUserId(Long userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}