package hpe.energy_optimization_backend.service;

import hpe.energy_optimization_backend.dto.request.UserLoginRequestDTO;
import hpe.energy_optimization_backend.dto.request.UserRegistrationRequestDTO;
import hpe.energy_optimization_backend.dto.response.UserLoginResponseDTO;
import hpe.energy_optimization_backend.dto.response.UserRegistrationResponseDTO;
import hpe.energy_optimization_backend.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface UserService {
    Map<String,Object> loginUser(UserLoginRequestDTO userLoginDTO);
    UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO userRegistrationDTO);
    UserDetails loadUserByUsername(String username);
    void clearCookies(HttpServletResponse response, String baseUrl);
    void resetPassword(String token, String newPassword);
    void forgotPassword(String email);
    void changePasswordById(Long userId, String oldPassword, String newPassword);
    Page<User> getUsersByFilters(String status, String searchTerm, Pageable pageable);
    void updateUserProfileStatus(Long userId, String profileStatus);

}