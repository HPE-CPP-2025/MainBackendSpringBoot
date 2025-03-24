// File: src/main/java/hpe/energy_optimization_backend/dto/DeviceCreateRequestDTO.java
package hpe.energy_optimization_backend.dto.request;

import lombok.Data;

@Data
public class DeviceRequestDTO {
    private String deviceName;
    private String deviceType;
    private String powerRating;
    private String location;
    private Long houseId;
}