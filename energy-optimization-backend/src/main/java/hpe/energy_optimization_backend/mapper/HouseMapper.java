package hpe.energy_optimization_backend.mapper;

import hpe.energy_optimization_backend.dto.request.HouseRequestDTO;
import hpe.energy_optimization_backend.dto.response.HouseResponseDTO;
import hpe.energy_optimization_backend.model.House;

import java.util.List;
import java.util.stream.Collectors;

public class HouseMapper {

    public static House toEntity(HouseRequestDTO dto) {
        House house = new House();
        house.setHouseName(dto.getHouseName());
        house.setLocation(dto.getLocation());
        return house;
    }

    public static HouseResponseDTO toResponseDTO(House house) {
        return HouseResponseDTO.builder()
                .houseId(house.getHouseId())
                .houseName(house.getHouseName())
                .location(house.getLocation())
                .build();
    }

    public static List<HouseResponseDTO> toResponseDTOList(List<House> houses) {
        return houses.stream()
                .map(HouseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public static void updateEntityFromDTO(House house, HouseRequestDTO dto) {
        house.setHouseName(dto.getHouseName());
        house.setLocation(dto.getLocation());
    }
}