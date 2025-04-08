package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.request.DeviceRequestDTO;
import hpe.energy_optimization_backend.dto.response.DeviceResponseDTO;
import hpe.energy_optimization_backend.service.DeviceService;
import hpe.energy_optimization_backend.urlMapper.DeviceUrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Device Management", description = "Endpoints for managing devices")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping(DeviceUrlMapping.GET_ALL)
    @Operation(
            summary = "Get all devices",
            description = "Retrieves a list of all devices."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of devices retrieved successfully", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices() {
        List<DeviceResponseDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping(DeviceUrlMapping.GET_BY_ID)
    @Operation(
            summary = "Get device by ID",
            description = "Retrieves a specific device by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device retrieved successfully", content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DeviceResponseDTO> getDeviceById(
            @Parameter(description = "ID of the device to retrieve") @PathVariable Long id) {
        DeviceResponseDTO device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @PostMapping(DeviceUrlMapping.CREATE)
    @Operation(
            summary = "Create a new device",
            description = "Creates a new device with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Device created successfully", content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DeviceResponseDTO> createDevice(
            @Validated @RequestBody DeviceRequestDTO dto) {
        DeviceResponseDTO createdDevice = deviceService.createDevice(dto);
        return ResponseEntity.status(201).body(createdDevice);
    }

    @PutMapping(DeviceUrlMapping.UPDATE)
    @Operation(
            summary = "Update a device",
            description = "Updates an existing device with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device updated successfully", content = @Content(schema = @Schema(implementation = DeviceResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @Parameter(description = "ID of the device to update") @PathVariable Long id,
            @Validated @RequestBody DeviceRequestDTO dto) {
        DeviceResponseDTO updatedDevice = deviceService.updateDevice(id, dto);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping(DeviceUrlMapping.DELETE)
    @Operation(
            summary = "Delete a device",
            description = "Deletes a specific device by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "ID of the device to delete") @PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}