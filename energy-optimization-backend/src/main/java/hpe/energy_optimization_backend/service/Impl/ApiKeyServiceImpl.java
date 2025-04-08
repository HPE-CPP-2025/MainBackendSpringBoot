package hpe.energy_optimization_backend.service.Impl;

import hpe.energy_optimization_backend.dto.request.ApiKeyRequestDTO;
import hpe.energy_optimization_backend.dto.response.ApiKeyResponseDTO;
import hpe.energy_optimization_backend.mapper.ApiKeyMapper;
import hpe.energy_optimization_backend.model.ApiKey;
import hpe.energy_optimization_backend.repository.ApiKeyRepository;
import hpe.energy_optimization_backend.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public ApiKeyResponseDTO generateApiKey(ApiKeyRequestDTO request) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String keyValue = "EnergyOpt_" + Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        ApiKey apiKey = apiKeyMapper.toEntity(request);
        apiKey.setKeyValue(keyValue);

        ApiKey savedKey = apiKeyRepository.save(apiKey);

        return apiKeyMapper.toResponseDTO(savedKey, true);
    }

    @Override
    public boolean validateApiKey(String apiKey) {
        Optional<ApiKey> foundKey = apiKeyRepository.findByKeyValueAndActiveIsTrue(apiKey);
        return foundKey.isPresent() && (foundKey.get().getExpiresAt() == null ||
                foundKey.get().getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Override
    public Optional<ApiKey> getApiKeyDetails(String apiKey) {
        return apiKeyRepository.findByKeyValueAndActiveIsTrue(apiKey);
    }

    @Override
    public List<ApiKeyResponseDTO> getAllApiKeys() {
        List<ApiKey> apiKeys = apiKeyRepository.findAll();
        return apiKeyMapper.toResponseDTOList(apiKeys);
    }

    @Override
    public List<ApiKeyResponseDTO> getApiKeysByStatus(Boolean active) {
        List<ApiKey> apiKeys;
        if (active == null) {
            apiKeys = apiKeyRepository.findAll();
        } else {
            apiKeys = apiKeyRepository.findByActive(active);
        }

        return apiKeyMapper.toResponseDTOList(apiKeys);
    }

    @Override
    public ApiKeyResponseDTO toggleApiKeyStatus(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Key not found with id: " + id));

        // Toggle the active status
        apiKey.setActive(!apiKey.isActive());
        ApiKey savedKey = apiKeyRepository.save(apiKey);

        return apiKeyMapper.toResponseDTO(savedKey, false);
    }

    @Override
    public ApiKeyResponseDTO getApiKeyWithFullKey(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Key not found with id: " + id));

        return apiKeyMapper.toResponseDTO(apiKey, true);
    }

    @Override
    public void deleteApiKey(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Key not found with id: " + id));
        apiKeyRepository.delete(apiKey);
    }
}