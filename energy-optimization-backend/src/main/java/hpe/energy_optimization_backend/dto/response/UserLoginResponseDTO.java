package hpe.energy_optimization_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResponseDTO {
    private Long userId;
    private Long homeId;
    private String email;
    private String username;
    private String role;
}