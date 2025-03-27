package hpe.energy_optimization_backend.config;

import hpe.energy_optimization_backend.security.CustomAccessDeniedHandler;
import hpe.energy_optimization_backend.security.filter.ApiKeyAuthFilter;
import hpe.energy_optimization_backend.security.filter.SseAuthenticationFilter;
import hpe.energy_optimization_backend.security.jwt.*;
import hpe.energy_optimization_backend.urlMapper.ApiKeyUrlMapping;
import hpe.energy_optimization_backend.urlMapper.UserUrlMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig {
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;
    private final CorsConfig corsConfig;
    private final SseAuthenticationFilter sseAuthenticationFilter;
    private final ApiKeyAuthFilter apiKeyAuthFilter;

    public SecurityConfig(
            AuthEntryPointJwt unauthorizedHandler,
            AuthTokenFilter authTokenFilter,
            CorsConfig corsConfig,
            SseAuthenticationFilter sseAuthenticationFilter,
            ApiKeyAuthFilter apiKeyAuthFilter
    ) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.authTokenFilter = authTokenFilter;
        this.corsConfig = corsConfig;
        this.sseAuthenticationFilter = sseAuthenticationFilter;
        this.apiKeyAuthFilter = apiKeyAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                UserUrlMapping.FORGOT_PASSWORD,
                                UserUrlMapping.RESET_PASSWORD,
                                UserUrlMapping.USER_REGISTER,
                                UserUrlMapping.USER_LOGIN,
                                UserUrlMapping.RENEW_TOKEN,
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api/auth/**",
                                "/api/public/**",
                                "/api/health",
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()
                        .requestMatchers(ApiKeyUrlMapping.BASE_API + "/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(sseAuthenticationFilter, AuthTokenFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}