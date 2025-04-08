package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.request.ApiKeyRequestDTO;
import hpe.energy_optimization_backend.dto.response.ApiKeyResponseDTO;
import hpe.energy_optimization_backend.service.ApiKeyService;
import hpe.energy_optimization_backend.urlMapper.ApiKeyUrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "API Key Management", description = "Endpoints for managing API keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(ApiKeyUrlMapping.GENERATE_API_KEY)
    @Operation(
            summary = "Generate a new API key",
            description = "Creates a new API key with the provided details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details for the new API key",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ApiKeyRequestDTO.class))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "API key successfully created", content = @Content(schema = @Schema(implementation = ApiKeyResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiKeyResponseDTO> generateApiKey(@RequestBody ApiKeyRequestDTO request) {
        log.info("Generating new API key: {}", request.getName());
        ApiKeyResponseDTO responseDTO = apiKeyService.generateApiKey(request);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(ApiKeyUrlMapping.GET_ALL_API_KEYS)
    @Operation(
            summary = "Get all API keys",
            description = "Retrieves a list of all API keys, optionally filtered by active status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of API keys retrieved", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<ApiKeyResponseDTO>> getAllApiKeys(
            @Parameter(description = "Filter by active status (true/false). If not provided, all keys are returned.")
            @RequestParam(required = false) Boolean active) {
        log.info("Retrieving API keys with filter - active: {}", active);
        List<ApiKeyResponseDTO> apiKeys = apiKeyService.getApiKeysByStatus(active);
        return ResponseEntity.ok(apiKeys);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(ApiKeyUrlMapping.TOGGLE_API_KEY_STATUS)
    @Operation(
            summary = "Toggle API key status",
            description = "Toggles the active status of an API key by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "API key status successfully toggled", content = @Content(schema = @Schema(implementation = ApiKeyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "API key not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiKeyResponseDTO> toggleApiKeyStatus(
            @Parameter(description = "ID of the API key to toggle")
            @PathVariable Long id) {
        log.info("Toggling API key status with ID: {}", id);
        ApiKeyResponseDTO updatedKey = apiKeyService.toggleApiKeyStatus(id);
        return ResponseEntity.ok(updatedKey);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(ApiKeyUrlMapping.GET_API_KEY_DETAILS)
    @Operation(
            summary = "Get API key details",
            description = "Retrieves the full details of an API key by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "API key details retrieved", content = @Content(schema = @Schema(implementation = ApiKeyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "API key not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<ApiKeyResponseDTO> getApiKeyDetails(
            @Parameter(description = "ID of the API key to retrieve")
            @PathVariable Long id) {
        log.info("Retrieving full API key details for ID: {}", id);
        ApiKeyResponseDTO apiKey = apiKeyService.getApiKeyWithFullKey(id);
        return ResponseEntity.ok(apiKey);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(ApiKeyUrlMapping.DELETE_API_KEY)
    @Operation(
            summary = "Delete an API key",
            description = "Deletes an API key by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "API key successfully deleted"),
            @ApiResponse(responseCode = "404", description = "API key not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> deleteApiKey(
            @Parameter(description = "ID of the API key to delete")
            @PathVariable Long id) {
        log.info("Deleting API key with ID: {}", id);
        apiKeyService.deleteApiKey(id);
        return ResponseEntity.noContent().build();
    }
}