package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.request.HouseRequestDTO;
import hpe.energy_optimization_backend.dto.response.HouseResponseDTO;
import hpe.energy_optimization_backend.service.HouseService;
import hpe.energy_optimization_backend.urlMapper.HouseUrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(HouseUrlMapping.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "House Management", description = "Endpoints for managing houses")
public class HouseController {

    private final HouseService houseService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(HouseUrlMapping.CREATE_HOUSE)
    @Operation(
            summary = "Create a new house",
            description = "Creates a new house with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "House created successfully", content = @Content(schema = @Schema(implementation = HouseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HouseResponseDTO> createHouse(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the house to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = HouseRequestDTO.class))
            )
            @RequestBody HouseRequestDTO houseRequestDTO) {
        return new ResponseEntity<>(houseService.createHouse(houseRequestDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(HouseUrlMapping.GET_HOUSE)
    @Operation(
            summary = "Get house by ID",
            description = "Retrieves a specific house by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "House retrieved successfully", content = @Content(schema = @Schema(implementation = HouseResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HouseResponseDTO> getHouse(
            @Parameter(description = "ID of the house to retrieve") @PathVariable Long houseId) {
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(HouseUrlMapping.GET_ALL_HOUSES)
    @Operation(
            summary = "Get all houses",
            description = "Retrieves a list of all houses."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of houses retrieved successfully", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<HouseResponseDTO>> getAllHouses() {
        return ResponseEntity.ok(houseService.getAllHouses());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(HouseUrlMapping.UPDATE_HOUSE)
    @Operation(
            summary = "Update a house",
            description = "Updates an existing house with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "House updated successfully", content = @Content(schema = @Schema(implementation = HouseResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HouseResponseDTO> updateHouse(
            @Parameter(description = "ID of the house to update") @PathVariable Long houseId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated details of the house",
                    required = true,
                    content = @Content(schema = @Schema(implementation = HouseRequestDTO.class))
            )
            @RequestBody HouseRequestDTO houseRequestDTO) {
        return ResponseEntity.ok(houseService.updateHouse(houseId, houseRequestDTO));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(HouseUrlMapping.DELETE_HOUSE)
    @Operation(
            summary = "Delete a house",
            description = "Deletes a specific house by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "House deleted successfully"),
            @ApiResponse(responseCode = "404", description = "House not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteHouse(
            @Parameter(description = "ID of the house to delete") @PathVariable Long houseId) {
        houseService.deleteHouse(houseId);
        return ResponseEntity.noContent().build();
    }
}