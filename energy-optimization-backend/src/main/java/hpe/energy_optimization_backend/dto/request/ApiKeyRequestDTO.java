package hpe.energy_optimization_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyRequestDTO {
    private String name;
    private String description;
    private LocalDateTime expiresAt;
}