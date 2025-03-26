package hpe.energy_optimization_backend.repository;

import hpe.energy_optimization_backend.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByKeyValueAndActiveIsTrue(String keyValue);
    List<ApiKey> findByActive(boolean active);
}