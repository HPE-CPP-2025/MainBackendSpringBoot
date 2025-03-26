package hpe.energy_optimization_backend.mapper;

import hpe.energy_optimization_backend.dto.request.ApiKeyRequestDTO;
import hpe.energy_optimization_backend.dto.response.ApiKeyResponseDTO;
import hpe.energy_optimization_backend.model.ApiKey;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiKeyMapper {

    public ApiKey toEntity(ApiKeyRequestDTO requestDTO) {
        return ApiKey.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .expiresAt(requestDTO.getExpiresAt())
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
    }

    public ApiKeyResponseDTO toResponseDTO(ApiKey apiKey, boolean includeFullKey) {
        return ApiKeyResponseDTO.builder()
                .id(apiKey.getId())
                .name(apiKey.getName())
                .description(apiKey.getDescription())
                .keyValue(includeFullKey ? apiKey.getKeyValue() : maskApiKey(apiKey.getKeyValue()))
                .createdAt(apiKey.getCreatedAt())
                .expiresAt(apiKey.getExpiresAt())
                .active(apiKey.isActive())
                .build();
    }

    public List<ApiKeyResponseDTO> toResponseDTOList(List<ApiKey> apiKeys) {
        return apiKeys.stream()
                .map(key -> toResponseDTO(key, false))
                .collect(Collectors.toList());
    }

    private String maskApiKey(String keyValue) {
        if (keyValue == null || keyValue.length() < 16) {
            return "********";
        }
        // Show only first 8 and last 4 characters
        return keyValue.substring(0, 8) + "..." + keyValue.substring(keyValue.length() - 4);
    }
}