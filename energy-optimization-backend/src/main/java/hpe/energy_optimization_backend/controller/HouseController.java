package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.request.HouseRequestDTO;
import hpe.energy_optimization_backend.dto.response.HouseResponseDTO;
import hpe.energy_optimization_backend.service.HouseService;
import hpe.energy_optimization_backend.urlMapper.HouseUrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(HouseUrlMapping.BASE_URL)
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping(HouseUrlMapping.CREATE_HOUSE)
    public ResponseEntity<HouseResponseDTO> createHouse(@RequestBody HouseRequestDTO houseRequestDTO) {
        return new ResponseEntity<>(houseService.createHouse(houseRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping(HouseUrlMapping.GET_HOUSE)
    public ResponseEntity<HouseResponseDTO> getHouse(@PathVariable Long houseId) {
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }

    @GetMapping(HouseUrlMapping.GET_ALL_HOUSES)
    public ResponseEntity<List<HouseResponseDTO>> getAllHouses() {
        return ResponseEntity.ok(houseService.getAllHouses());
    }

    @PutMapping(HouseUrlMapping.UPDATE_HOUSE)
    public ResponseEntity<HouseResponseDTO> updateHouse(
            @PathVariable Long houseId,
            @RequestBody HouseRequestDTO houseRequestDTO) {
        return ResponseEntity.ok(houseService.updateHouse(houseId, houseRequestDTO));
    }

    @DeleteMapping(HouseUrlMapping.DELETE_HOUSE)
    public ResponseEntity<Void> deleteHouse(@PathVariable Long houseId) {
        houseService.deleteHouse(houseId);
        return ResponseEntity.noContent().build();
    }


}