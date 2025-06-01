package hpe.energy_optimization_backend.service;

import hpe.energy_optimization_backend.dto.response.DeviceStatusDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface DeviceStatusService {
    DeviceStatusDTO toggleDeviceStatus(Long deviceId);
    SseEmitter subscribeToHouseUpdates(Long houseId);
    List<DeviceStatusDTO> getDeviceStatusForHouse(Long houseId);
    void refreshDeviceDataForHouse(Long houseId);
}