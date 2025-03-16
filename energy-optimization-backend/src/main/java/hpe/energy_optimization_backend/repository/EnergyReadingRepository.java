package hpe.energy_optimization_backend.repository;

import hpe.energy_optimization_backend.model.EnergyReading;
import hpe.energy_optimization_backend.model.EnergyReadingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyReadingRepository extends JpaRepository<EnergyReading, EnergyReadingId> {
}