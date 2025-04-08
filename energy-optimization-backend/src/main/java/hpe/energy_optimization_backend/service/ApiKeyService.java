package hpe.energy_optimization_backend.service;

import hpe.energy_optimization_backend.dto.request.ApiKeyRequestDTO;
import hpe.energy_optimization_backend.dto.response.ApiKeyResponseDTO;
import hpe.energy_optimization_backend.model.ApiKey;

import java.util.List;
import java.util.Optional;

public interface ApiKeyService {
    ApiKeyResponseDTO generateApiKey(ApiKeyRequestDTO request);
    boolean validateApiKey(String apiKey);
    Optional<ApiKey> getApiKeyDetails(String apiKey);
    List<ApiKeyResponseDTO> getAllApiKeys();
    List<ApiKeyResponseDTO> getApiKeysByStatus(Boolean active);
    ApiKeyResponseDTO toggleApiKeyStatus(Long id);
    ApiKeyResponseDTO getApiKeyWithFullKey(Long id);
    void deleteApiKey(Long id);
}