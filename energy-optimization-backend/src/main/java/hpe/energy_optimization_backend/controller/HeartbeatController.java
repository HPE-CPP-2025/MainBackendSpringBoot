package hpe.energy_optimization_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "System Monitoring", description = "Endpoints for monitoring the health of the API")
public class HeartbeatController {

    @GetMapping("/api/health")
    @Operation(
            summary = "Health check",
            description = "Checks the health status of the Energy Optimization API."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "API is running and healthy", content = @Content(schema = @Schema(example = "{ \"status\": \"UP\", \"timestamp\": \"2023-01-01T12:00:00\", \"message\": \"Energy Optimization API is running\" }"))),
            @ApiResponse(responseCode = "500", description = "API is not healthy")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Energy Optimization API is running");

        return ResponseEntity.ok(response);
    }
}