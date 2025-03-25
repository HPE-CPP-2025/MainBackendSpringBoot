package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.response.DeviceStatusDTO;
import hpe.energy_optimization_backend.service.DeviceStatusService;
import hpe.energy_optimization_backend.urlMapper.DeviceStatusUrlMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeviceStatusController {

    private final DeviceStatusService deviceStatusService;

    @PostMapping(DeviceStatusUrlMapping.TOGGLE_DEVICE)
    public ResponseEntity<DeviceStatusDTO> toggleDeviceStatus(@PathVariable Long deviceId) {
        log.info("Toggling device: {}", deviceId);
        return ResponseEntity.ok(deviceStatusService.toggleDeviceStatus(deviceId));
    }

    @GetMapping(path = DeviceStatusUrlMapping.SUBSCRIBE_HOUSE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToHouse(@PathVariable Long houseId) {
        log.info("New SSE subscription for house: {}", houseId);
        return deviceStatusService.subscribeToHouseUpdates(houseId);
    }

    @GetMapping(DeviceStatusUrlMapping.GET_HOUSE_DEVICE_STATUS)
    public ResponseEntity<List<DeviceStatusDTO>> getHouseDeviceStatus(@PathVariable Long houseId) {
        log.info("Getting device status for house: {}", houseId);
        return ResponseEntity.ok(deviceStatusService.getDeviceStatusForHouse(houseId));
    }
}