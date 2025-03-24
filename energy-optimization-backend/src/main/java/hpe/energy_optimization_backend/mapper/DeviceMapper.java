// File: src/main/java/hpe/energy_optimization_backend/mapper/DeviceMapper.java
package hpe.energy_optimization_backend.mapper;

import hpe.energy_optimization_backend.dto.response.DeviceResponseDTO;
import hpe.energy_optimization_backend.dto.request.DeviceRequestDTO;
import hpe.energy_optimization_backend.model.Device;
import hpe.energy_optimization_backend.model.House;

public class DeviceMapper {

    public static DeviceResponseDTO toDeviceResponseDTO(Device device) {
        DeviceResponseDTO dto = new DeviceResponseDTO();
        dto.setDeviceId(device.getDeviceId());
        dto.setDeviceName(device.getDeviceName());
        dto.setDeviceType(device.getDeviceType());
        dto.setPowerRating(device.getPowerRating());
        dto.setLocation(device.getLocation());
        dto.setHouseId(device.getHouse() != null ? device.getHouse().getHouseId() : null);
        return dto;
    }

    public static Device toDevice(DeviceRequestDTO dto, House house) {
        Device device = new Device();
        device.setDeviceName(dto.getDeviceName());
        device.setDeviceType(dto.getDeviceType());
        device.setPowerRating(dto.getPowerRating());
        device.setLocation(dto.getLocation());
        device.setHouse(house);
        return device;
    }
}