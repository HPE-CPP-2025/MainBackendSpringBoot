package hpe.energy_optimization_backend.mapper;

import hpe.energy_optimization_backend.dto.request.UserRegistrationRequestDTO;
import hpe.energy_optimization_backend.dto.response.UserLoginResponseDTO;
import hpe.energy_optimization_backend.dto.response.UserRegistrationResponseDTO;
import hpe.energy_optimization_backend.enums.Role;
import hpe.energy_optimization_backend.model.User;
import lombok.Builder;

@Builder
public class UserMapper {
    public static User toUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        return User.builder()
                .email(userRegistrationRequestDTO.getEmail())
                .password(userRegistrationRequestDTO.getPassword())
                .username(userRegistrationRequestDTO.getUsername())
                .build();
    }

    public static UserLoginResponseDTO toUserLoginResponseDTO(User user) {
        return UserLoginResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    public static User toEntity(UserRegistrationRequestDTO dto, String encodedPassword) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encodedPassword);
        user.setRole(Role.HOUSE_OWNER);
        user.setEmail(dto.getEmail());
        return user;
    }

    public static UserRegistrationResponseDTO toRegistrationResponseDTO(User user) {
        return new UserRegistrationResponseDTO(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole()
        );
    }
}
