package hpe.energy_optimization_backend.service.Impl;

import hpe.energy_optimization_backend.dto.request.DeviceRequestDTO;
import hpe.energy_optimization_backend.dto.response.DeviceResponseDTO;
import hpe.energy_optimization_backend.exception.houseAndDevice.DeviceNotFoundException;
import hpe.energy_optimization_backend.exception.houseAndDevice.HouseNotFoundException;
import hpe.energy_optimization_backend.exception.user.UnauthorizedAccessException;
import hpe.energy_optimization_backend.mapper.DeviceMapper;
import hpe.energy_optimization_backend.model.Device;
import hpe.energy_optimization_backend.model.House;
import hpe.energy_optimization_backend.repository.DeviceRepository;
import hpe.energy_optimization_backend.repository.HouseRepository;
import hpe.energy_optimization_backend.security.jwt.JwtUtils;
import hpe.energy_optimization_backend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final HouseRepository houseRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, HouseRepository houseRepository, JwtUtils jwtUtils) {
        this.deviceRepository = deviceRepository;
        this.houseRepository = houseRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public List<DeviceResponseDTO> getAllDevices() {
        // Only house owners can see their devices
        Long houseId = jwtUtils.getHouseIdFromContext();
        if (houseId == null) {
            throw new UnauthorizedAccessException("You don't have permission to access any devices");
        }

        return deviceRepository.findByHouse_HouseId(houseId)
                .stream()
                .map(DeviceMapper::toDeviceResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceResponseDTO getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        // Only house owner can access their devices
        Long userHouseId = jwtUtils.getHouseIdFromContext();
        if (userHouseId == null || !device.getHouse().getHouseId().equals(userHouseId)) {
            throw new UnauthorizedAccessException("You don't have permission to access this device");
        }

        return DeviceMapper.toDeviceResponseDTO(device);
    }

    @Override
    public DeviceResponseDTO createDevice(DeviceRequestDTO dto) {
        // Only house owner can create devices for their house
        Long userHouseId = jwtUtils.getHouseIdFromContext();
        if (userHouseId == null || !dto.getHouseId().equals(userHouseId)) {
            throw new UnauthorizedAccessException("You can only create devices for your own house");
        }

        House house = houseRepository.findById(dto.getHouseId())
                .orElseThrow(() -> new HouseNotFoundException("House not found"));
        Device device = DeviceMapper.toDevice(dto, house);
        return DeviceMapper.toDeviceResponseDTO(deviceRepository.save(device));
    }

    @Override
    public DeviceResponseDTO updateDevice(Long id, DeviceRequestDTO dto) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        // Only house owner can update their devices
        Long userHouseId = jwtUtils.getHouseIdFromContext();
        if (userHouseId == null ||
                !device.getHouse().getHouseId().equals(userHouseId) ||
                !dto.getHouseId().equals(userHouseId)) {
            throw new UnauthorizedAccessException("You don't have permission to update this device");
        }

        House house = houseRepository.findById(dto.getHouseId())
                .orElseThrow(() -> new HouseNotFoundException("House not found"));
        device.setDeviceName(dto.getDeviceName());
        device.setDeviceType(dto.getDeviceType());
        device.setPowerRating(dto.getPowerRating());
        device.setLocation(dto.getLocation());
        device.setHouse(house);
        return DeviceMapper.toDeviceResponseDTO(deviceRepository.save(device));
    }

    @Override
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        // Only house owner can delete their devices
        Long userHouseId = jwtUtils.getHouseIdFromContext();
        if (userHouseId == null || !device.getHouse().getHouseId().equals(userHouseId)) {
            throw new UnauthorizedAccessException("You don't have permission to delete this device");
        }

        deviceRepository.deleteById(id);
    }
}