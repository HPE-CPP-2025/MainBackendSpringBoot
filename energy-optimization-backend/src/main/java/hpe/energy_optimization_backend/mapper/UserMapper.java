package hpe.energy_optimization_backend.mapper;

import hpe.energy_optimization_backend.dto.request.UserDetailsSummaryResponseDTO;
import hpe.energy_optimization_backend.dto.request.UserRegistrationRequestDTO;
import hpe.energy_optimization_backend.dto.response.UserLoginResponseDTO;
import hpe.energy_optimization_backend.dto.response.UserRegistrationResponseDTO;
import hpe.energy_optimization_backend.enums.ProfileStatus;
import hpe.energy_optimization_backend.enums.Role;
import hpe.energy_optimization_backend.exception.houseAndDevice.HouseNotFoundException;
import hpe.energy_optimization_backend.model.House;
import hpe.energy_optimization_backend.model.User;
import hpe.energy_optimization_backend.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final HouseRepository houseRepository;

    public User toUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        User user = User.builder()
                .email(userRegistrationRequestDTO.getEmail())
                .password(userRegistrationRequestDTO.getPassword())
                .username(userRegistrationRequestDTO.getUsername())
                .build();

        if (userRegistrationRequestDTO.getHouseId() != null && !userRegistrationRequestDTO.getHouseId().isEmpty()) {
            House house = houseRepository.findById(Long.parseLong(userRegistrationRequestDTO.getHouseId()))
                    .orElseThrow(() -> new HouseNotFoundException("House not found with id: " + userRegistrationRequestDTO.getHouseId()));
            user.setHouse(house);
        }

        return user;
    }

    public User toEntity(UserRegistrationRequestDTO dto, String encodedPassword) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encodedPassword);
        user.setRole(Role.HOUSE_OWNER);
        user.setProfileStatus(ProfileStatus.INACTIVE);
        user.setEmail(dto.getEmail());

        if (dto.getHouseId() == null || dto.getHouseId().isEmpty()) {
            throw new HouseNotFoundException("House ID is required for registration");
        }

        House house = houseRepository.findById(Long.parseLong(dto.getHouseId()))
                .orElseThrow(() -> new HouseNotFoundException("House not found with id: " + dto.getHouseId()));
        user.setHouse(house);

        return user;
    }

    public static UserLoginResponseDTO toUserLoginResponseDTO(User user) {
        UserLoginResponseDTO responseDTO = UserLoginResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();

        if (user.getHouse() != null) {
            responseDTO.setHouseId(user.getHouse().getHouseId());
        }

        return responseDTO;
    }

    public static UserDetailsSummaryResponseDTO toUserDetailsSummaryResponseDTO(User user) {
        return new UserDetailsSummaryResponseDTO(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getProfileStatus().name()
        );
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