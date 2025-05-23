package hpe.energy_optimization_backend.repository;

import hpe.energy_optimization_backend.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByHouse_HouseId(Long houseId);

}