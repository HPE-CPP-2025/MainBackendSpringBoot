package hpe.energy_optimization_backend.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusDTO {
    private Long deviceId;
    private String deviceName;
    private String deviceType;
    private String powerRating;
    private String location;
    private boolean isOn;
    private LocalDateTime turnedOnAt;
}