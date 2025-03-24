package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.request.DeviceRequestDTO;
import hpe.energy_optimization_backend.dto.response.DeviceResponseDTO;
import hpe.energy_optimization_backend.service.DeviceService;
import hpe.energy_optimization_backend.urlMapper.DeviceUrlMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    @GetMapping(DeviceUrlMapping.GET_ALL)
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices() {
        List<DeviceResponseDTO> devices =
                deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping(DeviceUrlMapping.GET_BY_ID)
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable Long id) {
        DeviceResponseDTO device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @PostMapping(DeviceUrlMapping.CREATE)
    public ResponseEntity<DeviceResponseDTO> createDevice(@Validated @RequestBody DeviceRequestDTO dto) {
        DeviceResponseDTO createdDevice = deviceService.createDevice(dto);
        return ResponseEntity.status(201).body(createdDevice);
    }

    @PutMapping(DeviceUrlMapping.UPDATE)
    public ResponseEntity<DeviceResponseDTO> updateDevice(@PathVariable Long id,
                                                          @Validated @RequestBody DeviceRequestDTO dto) {
        DeviceResponseDTO updatedDevice = deviceService.updateDevice(id, dto);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping(DeviceUrlMapping.DELETE)
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}