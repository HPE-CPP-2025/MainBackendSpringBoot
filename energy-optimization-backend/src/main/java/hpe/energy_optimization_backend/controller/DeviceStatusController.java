package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.response.DeviceStatusDTO;
import hpe.energy_optimization_backend.service.DeviceStatusService;
import hpe.energy_optimization_backend.urlMapper.DeviceStatusUrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Device Status Management", description = "Endpoints for managing and monitoring device statuses")
public class DeviceStatusController {

    private final DeviceStatusService deviceStatusService;

    @PostMapping(DeviceStatusUrlMapping.TOGGLE_DEVICE)
    @Operation(
            summary = "Toggle device status",
            description = "Toggles the status of a specific device by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device status toggled successfully", content = @Content(schema = @Schema(implementation = DeviceStatusDTO.class))),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DeviceStatusDTO> toggleDeviceStatus(
            @Parameter(description = "ID of the device to toggle") @PathVariable Long deviceId) {
        log.info("Toggling device: {}", deviceId);
        return ResponseEntity.ok(deviceStatusService.toggleDeviceStatus(deviceId));
    }

    @GetMapping(path = DeviceStatusUrlMapping.SUBSCRIBE_HOUSE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(
            summary = "Subscribe to house updates",
            description = "Subscribes to real-time updates for a specific house using Server-Sent Events (SSE)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subscription established successfully"),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public SseEmitter subscribeToHouse(
            @Parameter(description = "ID of the house to subscribe to") @PathVariable Long houseId) {
        log.info("New SSE subscription for house: {}", houseId);
        return deviceStatusService.subscribeToHouseUpdates(houseId);
    }

    @GetMapping(DeviceStatusUrlMapping.GET_HOUSE_DEVICE_STATUS)
    @Operation(
            summary = "Get house device statuses",
            description = "Retrieves the statuses of all devices in a specific house."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device statuses retrieved successfully", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DeviceStatusDTO>> getHouseDeviceStatus(
            @Parameter(description = "ID of the house to retrieve device statuses for") @PathVariable Long houseId) {
        log.info("Getting device status for house: {}", houseId);
        return ResponseEntity.ok(deviceStatusService.getDeviceStatusForHouse(houseId));
    }
}