package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.request.ApiKeyRequestDTO;
import hpe.energy_optimization_backend.dto.response.ApiKeyResponseDTO;
import hpe.energy_optimization_backend.service.ApiKeyService;
import hpe.energy_optimization_backend.urlMapper.ApiKeyUrlMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(ApiKeyUrlMapping.GENERATE_API_KEY)
    public ResponseEntity<ApiKeyResponseDTO> generateApiKey(@RequestBody ApiKeyRequestDTO request) {
        log.info("Generating new API key: {}", request.getName());
        ApiKeyResponseDTO responseDTO = apiKeyService.generateApiKey(request);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(ApiKeyUrlMapping.GET_ALL_API_KEYS)
    public ResponseEntity<List<ApiKeyResponseDTO>> getAllApiKeys(
            @RequestParam(required = false) Boolean active) {
        log.info("Retrieving API keys with filter - active: {}", active);
        List<ApiKeyResponseDTO> apiKeys = apiKeyService.getApiKeysByStatus(active);
        return ResponseEntity.ok(apiKeys);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(ApiKeyUrlMapping.TOGGLE_API_KEY_STATUS)
    public ResponseEntity<ApiKeyResponseDTO> toggleApiKeyStatus(@PathVariable Long id) {
        log.info("Toggling API key status with ID: {}", id);
        ApiKeyResponseDTO updatedKey = apiKeyService.toggleApiKeyStatus(id);
        return ResponseEntity.ok(updatedKey);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(ApiKeyUrlMapping.GET_API_KEY_DETAILS)
    public ResponseEntity<ApiKeyResponseDTO> getApiKeyDetails(@PathVariable Long id) {
        log.info("Retrieving full API key details for ID: {}", id);
        ApiKeyResponseDTO apiKey = apiKeyService.getApiKeyWithFullKey(id);
        return ResponseEntity.ok(apiKey);
    }
}