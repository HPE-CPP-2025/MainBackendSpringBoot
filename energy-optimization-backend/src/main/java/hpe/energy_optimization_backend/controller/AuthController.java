package hpe.energy_optimization_backend.controller;


import hpe.energy_optimization_backend.dto.request.*;
import hpe.energy_optimization_backend.dto.response.ResetPasswordResponseDTO;
import hpe.energy_optimization_backend.dto.response.UserLoginResponseDTO;
import hpe.energy_optimization_backend.dto.response.UserRegistrationResponseDTO;
import hpe.energy_optimization_backend.dto.response.VerifyEmailResponseDTO;
import hpe.energy_optimization_backend.exception.token.MissingRequestCookieException;
import hpe.energy_optimization_backend.exception.user.UserNotFoundException;
import hpe.energy_optimization_backend.mapper.UserMapper;
import hpe.energy_optimization_backend.model.User;
import hpe.energy_optimization_backend.security.jwt.JwtUtils;
import hpe.energy_optimization_backend.service.Impl.RefreshTokenServiceImpl;
import hpe.energy_optimization_backend.service.UserService;
import hpe.energy_optimization_backend.urlMapper.UserUrlMapping;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
//import hpe.energy_optimization_backend.dto.response.VerifyEmailResponseDTO;
//import hpe.energy_optimization_backend.dto.request.VerifyEmailRequestDTO;
//import hpe.energy_optimization_backend.dto.request.ResetPasswordRequestDTO;
import java.util.Map;

@RestController
@Slf4j
public class AuthController {
    private final String baseUrl;
    private final UserService userService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService,
                          RefreshTokenServiceImpl refreshTokenService,
                          @Value("${spring.app.base-url}") String baseUrl, JwtUtils jwtUtils) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.baseUrl = baseUrl;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping(UserUrlMapping.USER_REGISTER)
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(@RequestBody UserRegistrationRequestDTO userRegistrationDTO) {
        UserRegistrationResponseDTO userDTO = userService.registerUser(userRegistrationDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping(UserUrlMapping.USER_LOGIN)
    public ResponseEntity<?> authenticateUser(
            @RequestBody UserLoginRequestDTO loginRequest,
            HttpServletResponse response) {
        log.debug("Processing login request for email: {}", loginRequest.getEmail());
        Map<String, Object> loginResponse = userService.loginUser(loginRequest);
        String accessToken = (String) loginResponse.get("accessToken");
        String refreshToken = (String) loginResponse.get("refreshToken");
        UserLoginResponseDTO userDTO = (UserLoginResponseDTO) loginResponse.get("user");

        if (accessToken == null || refreshToken == null || userDTO == null) {
            log.error("Login response missing required fields");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed due to internal error");
        }
        refreshTokenService.setSecureRefreshTokenCookie(response, refreshToken);
        String bearerToken = "Bearer " + accessToken;

        log.info("User successfully authenticated: {}", userDTO.getEmail());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(userDTO);
    }

    @PostMapping(UserUrlMapping.USER_LOGOUT)
    public ResponseEntity<String> logoutUser(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response) {

        log.info("Logout request received");

        if (refreshToken != null && !refreshToken.isEmpty()) {
            String email = refreshTokenService.getEmailFromRefreshToken(refreshToken);
            refreshTokenService.deleteRefreshToken(refreshToken);
            log.info("Refresh token deleted for user: {}", email);
        }

        SecurityContextHolder.clearContext();
        userService.clearCookies(response, baseUrl);
        return ResponseEntity.ok("User logged out successfully.");
    }


    @PostMapping(UserUrlMapping.FORGOT_PASSWORD)
    @PreAuthorize("permitAll()")
    public ResponseEntity<VerifyEmailResponseDTO> forgotPassword(@RequestBody VerifyEmailRequestDTO verifyEmailRequestDTO) {
        userService.forgotPassword(verifyEmailRequestDTO.getEmail());
        return ResponseEntity.ok(new VerifyEmailResponseDTO("Password reset email sent successfully."));
    }

    @PostMapping(UserUrlMapping.RESET_PASSWORD)
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResetPasswordResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
        userService.resetPassword(resetPasswordRequestDTO.getToken(), resetPasswordRequestDTO.getNewPassword());
        return ResponseEntity.ok(new ResetPasswordResponseDTO("Password has been reset successfully."));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(UserUrlMapping.CHANGE_PASSWORD)
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        Long userId = Long.valueOf(jwtUtils.getUserIdFromContext());

        try {
            userService.changePasswordById(userId, changePasswordRequestDTO.getOldPassword(), changePasswordRequestDTO.getNewPassword());
            return ResponseEntity.ok("Password changed successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(UserUrlMapping.RENEW_TOKEN)
    public ResponseEntity<?> renewToken(@CookieValue("refreshToken") String refreshToken,
                                        HttpServletResponse response) {
        if (refreshToken == null) {
            throw new MissingRequestCookieException("Required cookie 'refreshToken' is not present");
        }

        Map<String, Object> renewResponse = refreshTokenService.renewToken(refreshToken);

        String newAccessToken = (String) renewResponse.get("accessToken");
        String newRefreshToken = (String) renewResponse.get("refreshToken");
        UserLoginResponseDTO responseDTO = (UserLoginResponseDTO) renewResponse.get("user");

        // Set new refresh token as HttpOnly cookie
        refreshTokenService.setSecureRefreshTokenCookie(response, newRefreshToken);
        String bearerToken = "Bearer " + newAccessToken;

        // Return new access token in Authorization header and user data in body
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(responseDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UserUrlMapping.GET_ALL_USERS_BY_PROFILE_STATUS)
    public ResponseEntity<Page<UserDetailsSummaryResponseDTO>> getAllUsers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            Pageable pageable) {

        try {
            Page<User> users = userService.getUsersByFilters(status, search, pageable);
            Page<UserDetailsSummaryResponseDTO> response = users.map(UserMapper::toUserDetailsSummaryResponseDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving users", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}