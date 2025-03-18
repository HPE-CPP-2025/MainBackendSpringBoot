package hpe.energy_optimization_backend.dto.response;
import hpe.energy_optimization_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationResponseDTO {
    private Long userId;
    private String email;
    private String username;
    private Role role;
}