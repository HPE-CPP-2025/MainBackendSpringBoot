package hpe.energy_optimization_backend.service.Impl;

import hpe.energy_optimization_backend.dto.response.DeviceStatusDTO;
import hpe.energy_optimization_backend.exception.houseAndDevice.DeviceNotFoundException;
import hpe.energy_optimization_backend.mapper.DeviceStatusMapper;
import hpe.energy_optimization_backend.model.Device;
import hpe.energy_optimization_backend.repository.DeviceRepository;
import hpe.energy_optimization_backend.service.DeviceStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceStatusServiceImpl implements DeviceStatusService {

    private final DeviceRepository deviceRepository;
    private final DeviceStatusMapper deviceStatusMapper;
    private final CacheManager cacheManager;

    // Status tracking maps
    private final Map<Long, Boolean> deviceStatusMap = new ConcurrentHashMap<>();
    private final Map<Long, LocalDateTime> deviceOnTimeMap = new ConcurrentHashMap<>();

    // House-specific emitters
    private final Map<Long, List<SseEmitter>> houseEmitters = new ConcurrentHashMap<>();

    @Override
    public DeviceStatusDTO toggleDeviceStatus(Long deviceId) {
        // Get device and validate
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + deviceId));
        Long houseId = device.getHouse().getHouseId();

        // Toggle status
        boolean newStatus = !deviceStatusMap.getOrDefault(deviceId, false);
        deviceStatusMap.put(deviceId, newStatus);

        // Update on-time
        if (newStatus) {
            deviceOnTimeMap.put(deviceId, LocalDateTime.now());
        } else {
            deviceOnTimeMap.remove(deviceId);
        }

        // Create DTO
        DeviceStatusDTO statusDTO = deviceStatusMapper.toDeviceStatusDTO(
                device, newStatus, deviceOnTimeMap.get(deviceId));

        // Update cache
        updateDeviceInCache(houseId, statusDTO);

        // Broadcast update
        broadcastToHouse(houseId, statusDTO);

        return statusDTO;
    }

    @Override
    public SseEmitter subscribeToHouseUpdates(Long houseId) {
        log.info("New client subscribing to house: {}", houseId);
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // Get or create emitter list for this house
        List<SseEmitter> emitters = houseEmitters.computeIfAbsent(
                houseId, k -> new CopyOnWriteArrayList<>());

        // Setup cleanup handlers
        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                houseEmitters.remove(houseId);
            }
        });

        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                houseEmitters.remove(houseId);
            }
        });

        emitter.onError(e -> {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                houseEmitters.remove(houseId);
            }
        });

        emitters.add(emitter);

        try {
            // Send initial data - all current devices for this house
            List<DeviceStatusDTO> devices = getDeviceStatusesByHouseId(houseId);
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data(devices));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    // Helper methods
    private List<DeviceStatusDTO> getDeviceStatusesByHouseId(Long houseId) {
        // Try to get from cache first
        Cache cache = cacheManager.getCache("devicesCache");
        if (cache != null) {
            Cache.ValueWrapper cachedValue = cache.get(houseId);
            if (cachedValue != null) {
                return (List<DeviceStatusDTO>) cachedValue.get();
            }
        }

        // If not in cache, fetch from DB and build
        List<Device> devices = deviceRepository.findByHouse_HouseId(houseId);
        List<DeviceStatusDTO> statusList = deviceStatusMapper.toDeviceStatusDTOList(
                devices, deviceStatusMap, deviceOnTimeMap);

        // Store in cache
        if (cache != null) {
            cache.put(houseId, statusList);
        }

        return statusList;
    }

    private void updateDeviceInCache(Long houseId, DeviceStatusDTO updatedDevice) {
        Cache cache = cacheManager.getCache("devicesCache");
        if (cache == null) return;

        Cache.ValueWrapper cachedValue = cache.get(houseId);
        if (cachedValue != null) {
            List<DeviceStatusDTO> devices = (List<DeviceStatusDTO>) cachedValue.get();

            // Update the specific device in the list
            List<DeviceStatusDTO> updatedDevices = devices.stream()
                    .map(device -> {
                        if (device.getDeviceId().equals(updatedDevice.getDeviceId())) {
                            // Return the updated device with the correct turnedOnAt value
                            return updatedDevice;
                        }
                        return device;
                    })
                    .collect(Collectors.toList());

            cache.put(houseId, updatedDevices);
        } else {
            // If not in cache yet, fetch all and add to cache
            getDeviceStatusesByHouseId(houseId);
        }
    }

    private void broadcastToHouse(Long houseId, DeviceStatusDTO statusDTO) {
        List<SseEmitter> emitters = houseEmitters.get(houseId);
        if (emitters == null || emitters.isEmpty()) {
            return; // No subscribers for this house
        }

        // If statusDTO is null, this method was called from refreshDeviceDataForHouse,
        // so we should return to avoid infinite recursion
        if (statusDTO == null) {
            return;
        }

        // Get the full updated list of devices for this house
        List<DeviceStatusDTO> allDevices = getDeviceStatusesByHouseId(houseId);

        // Log the devices list for debugging
        log.debug("Broadcasting devices to house {}: {}", houseId, allDevices);

        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                // Send the complete device list instead of just the updated device
                emitter.send(SseEmitter.event()
                        .name("DEVICE_UPDATE")
                        .data(allDevices));
            } catch (Exception e) {
                deadEmitters.add(emitter);
                log.error("Failed to send SSE update to house {}", houseId, e);
            }
        }

        // Remove dead emitters
        if (!deadEmitters.isEmpty()) {
            emitters.removeAll(deadEmitters);
            if (emitters.isEmpty()) {
                houseEmitters.remove(houseId);
            }
        }
    }
    @Override
    public List<DeviceStatusDTO> getDeviceStatusForHouse(Long houseId) {
        // This simply calls our existing method that either returns from cache or builds from DB
        return getDeviceStatusesByHouseId(houseId);
    }

    @Override
    public void refreshDeviceDataForHouse(Long houseId) {
        log.info("Refreshing device data for house: {}", houseId);

        // Clear the cache for this house
        Cache cache = cacheManager.getCache("devicesCache");
        if (cache != null) {
            cache.evict(houseId);
        }

        // Get emitters for this house
        List<SseEmitter> emitters = houseEmitters.get(houseId);
        if (emitters != null && !emitters.isEmpty()) {

            List<SseEmitter> deadEmitters = new ArrayList<>();
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("CACHE_INVALIDATED")
                            .data(Map.of("houseId", houseId)));
                } catch (Exception e) {
                    deadEmitters.add(emitter);
                    log.error("Failed to send SSE update to house {}", houseId, e);
                }
            }

            // Remove dead emitters
            if (!deadEmitters.isEmpty()) {
                emitters.removeAll(deadEmitters);
                if (emitters.isEmpty()) {
                    houseEmitters.remove(houseId);
                }
            }
        }

        log.debug("Device data cache refreshed for house {}", houseId);
    }
}