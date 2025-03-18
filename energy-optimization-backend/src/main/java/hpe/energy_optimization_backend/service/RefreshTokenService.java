// backend/src/main/java/com/dbms/mentalhealth/service/RefreshTokenService.java
package hpe.energy_optimization_backend.service;
import hpe.energy_optimization_backend.model.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);
    boolean verifyRefreshToken(String token);
    void deleteRefreshToken(String token);
    String getEmailFromRefreshToken(String token);
    public Map<String,Object> renewToken(String refreshToken);
    void setSecureRefreshTokenCookie(HttpServletResponse response, String refreshToken);
}