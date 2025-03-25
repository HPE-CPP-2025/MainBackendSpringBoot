package hpe.energy_optimization_backend.mapper;

import hpe.energy_optimization_backend.dto.response.DeviceStatusDTO;
import hpe.energy_optimization_backend.model.Device;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceStatusMapper {

    public DeviceStatusDTO toDeviceStatusDTO(Device device, boolean isOn, LocalDateTime turnedOnAt) {
        DeviceStatusDTO dto = DeviceStatusDTO.builder()
                .deviceId(device.getDeviceId())
                .deviceName(device.getDeviceName())
                .deviceType(device.getDeviceType())
                .powerRating(device.getPowerRating())
                .location(device.getLocation())
                .isOn(isOn)
                .turnedOnAt(turnedOnAt)
                .build();
        return dto;
    }

    public List<DeviceStatusDTO> toDeviceStatusDTOList(List<Device> devices,
                                                       java.util.Map<Long, Boolean> deviceStatusMap,
                                                       java.util.Map<Long, LocalDateTime> deviceOnTimeMap) {

        return devices.stream()
                .map(device -> {
                    Long deviceId = device.getDeviceId();
                    boolean isOn = deviceStatusMap.getOrDefault(deviceId, false);
                    LocalDateTime turnedOnAt = isOn ? deviceOnTimeMap.get(deviceId) : null;
                    return toDeviceStatusDTO(device, isOn, turnedOnAt);
                })
                .collect(Collectors.toList());
    }


}