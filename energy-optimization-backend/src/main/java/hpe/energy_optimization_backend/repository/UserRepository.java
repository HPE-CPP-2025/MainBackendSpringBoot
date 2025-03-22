package hpe.energy_optimization_backend.repository;

import hpe.energy_optimization_backend.enums.ProfileStatus;
import hpe.energy_optimization_backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findByUserId(Long userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    @Query("SELECT u FROM User u WHERE " +
            "u.role = 'HOUSE_OWNER' AND " +
            "(:status IS NULL OR u.profileStatus = :status) AND " +
            "(:search IS NULL OR " +
            "LOWER(CAST(u.username AS string)) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findUsersWithFilters(
            @Param("status") ProfileStatus status,
            @Param("search") String search,
            Pageable pageable);
    @Query("SELECT u FROM User u WHERE " +
            "u.role = 'HOUSE_OWNER' AND " +
            "(:status IS NULL OR u.profileStatus = :status)")
    Page<User> findUsersWithFilters(
            @Param("status") ProfileStatus status,
            Pageable pageable);
}