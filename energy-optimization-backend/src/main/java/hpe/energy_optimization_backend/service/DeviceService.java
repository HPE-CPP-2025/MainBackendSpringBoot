package hpe.energy_optimization_backend.service;

import hpe.energy_optimization_backend.dto.request.DeviceRequestDTO;
import hpe.energy_optimization_backend.dto.response.DeviceResponseDTO;

import java.util.List;

public interface DeviceService {
    List<DeviceResponseDTO> getAllDevices();

    DeviceResponseDTO getDeviceById(Long id);

    DeviceResponseDTO createDevice(DeviceRequestDTO dto);

    DeviceResponseDTO updateDevice(Long id, DeviceRequestDTO dto);

    void deleteDevice(Long id);

}
