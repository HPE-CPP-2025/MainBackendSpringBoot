package hpe.energy_optimization_backend.service;

import hpe.energy_optimization_backend.dto.request.HouseRequestDTO;
import hpe.energy_optimization_backend.dto.response.HouseResponseDTO;

import java.util.List;

public interface HouseService {
    HouseResponseDTO createHouse(HouseRequestDTO houseRequestDTO);
    HouseResponseDTO getHouse(Long houseId);
    List<HouseResponseDTO> getAllHouses();
    HouseResponseDTO updateHouse(Long houseId, HouseRequestDTO houseRequestDTO);
    void deleteHouse(Long houseId);
    void assignUserToHouse(Long houseId, Long userId);
}