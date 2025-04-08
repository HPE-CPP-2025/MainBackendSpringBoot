package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.request.ForwardRequestDTO;
import hpe.energy_optimization_backend.service.FastApiService;
import hpe.energy_optimization_backend.urlMapper.AgentUrlMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Request Forwarding", description = "Endpoints for forwarding requests to external services")
public class ForwardController {

    private final FastApiService fastApiService;

    public ForwardController(FastApiService fastApiService) {
        this.fastApiService = fastApiService;
    }

    @PostMapping(AgentUrlMapper.AGENT_URL)
    @Operation(
            summary = "Forward a request",
            description = "Forwards a request to an external service based on the provided query and role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request forwarded successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String forwardRequest(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the request to forward",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ForwardRequestDTO.class))
            )
            @RequestBody ForwardRequestDTO requestDTO) {
        String query = requestDTO.getQuery();
        String role = requestDTO.getRole();
        Long houseId = "HOUSE_OWNER".equals(role) ? 1L : null;
        return fastApiService.forwardRequest(query, houseId);
    }
}