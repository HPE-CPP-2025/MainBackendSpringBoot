package hpe.energy_optimization_backend.service;

import hpe.energy_optimization_backend.model.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface FastApiService {
    String forwardRequest(String query, Long house_id);
}