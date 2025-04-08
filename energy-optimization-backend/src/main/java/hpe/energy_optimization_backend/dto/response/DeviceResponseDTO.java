package hpe.energy_optimization_backend.dto.response;

import lombok.Data;

@Data
public class DeviceResponseDTO {
    private Long deviceId;
    private String deviceName;
    private String deviceType;
    private String powerRating;
    private String location;
    private Long houseId;
}