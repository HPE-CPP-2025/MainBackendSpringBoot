package hpe.energy_optimization_backend.service;

import hpe.energy_optimization_backend.dto.request.UserLoginRequestDTO;
import hpe.energy_optimization_backend.dto.request.UserRegistrationRequestDTO;
import hpe.energy_optimization_backend.dto.response.UserLoginResponseDTO;
import hpe.energy_optimization_backend.dto.response.UserRegistrationResponseDTO;
import hpe.energy_optimization_backend.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface UserService {
    Map<String,Object> loginUser(UserLoginRequestDTO userLoginDTO);
    UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO userRegistrationDTO);
    UserDetails loadUserByUsername(String username);
    void clearCookies(HttpServletResponse response, String baseUrl);
}