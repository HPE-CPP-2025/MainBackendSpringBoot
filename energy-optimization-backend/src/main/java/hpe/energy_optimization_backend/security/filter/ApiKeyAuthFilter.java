package hpe.energy_optimization_backend.security.filter;

import hpe.energy_optimization_backend.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;
    private static final String API_KEY_HEADER = "x-api-key";

    // Custom wrapper for filter chain
    private static class CustomFilterChain implements FilterChain {
        @Override
        public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response)
                throws IOException, ServletException {
            // Do nothing - stops the filter chain
        }
    }

    private static final FilterChain EMPTY_CHAIN = new CustomFilterChain();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Only apply to device status API endpoints
        if (path.startsWith("/api/device-status")) {
            // Extract API key from header
            String apiKey = request.getHeader(API_KEY_HEADER);

            // If API key is present, validate it
            if (apiKey != null && !apiKey.isEmpty()) {
                if (apiKeyService.validateApiKey(apiKey)) {
                    log.debug("Valid API key used for path: {}", path);

                    // Create authentication token with API_CLIENT role
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            "api-client",
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_CLIENT"))
                    );

                    // Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Process the request directly - subsequent filters won't re-authenticate
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    // Invalid API key
                    log.warn("Invalid API key used for path: {}", path);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid API key");
                    return;
                }
            }

        }

        // Continue the filter chain for non-device endpoints or device endpoints without API key
        filterChain.doFilter(request, response);
    }
}