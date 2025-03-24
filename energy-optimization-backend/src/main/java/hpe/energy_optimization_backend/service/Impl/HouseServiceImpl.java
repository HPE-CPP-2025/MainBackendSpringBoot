package hpe.energy_optimization_backend.service.Impl;

import hpe.energy_optimization_backend.dto.request.HouseRequestDTO;
import hpe.energy_optimization_backend.dto.response.HouseResponseDTO;
import hpe.energy_optimization_backend.exception.houseAndDevice.HouseNotFoundException;
import hpe.energy_optimization_backend.exception.user.UserNotFoundException;
import hpe.energy_optimization_backend.mapper.HouseMapper;
import hpe.energy_optimization_backend.model.House;
import hpe.energy_optimization_backend.model.User;
import hpe.energy_optimization_backend.repository.HouseRepository;
import hpe.energy_optimization_backend.repository.UserRepository;
import hpe.energy_optimization_backend.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public HouseResponseDTO createHouse(HouseRequestDTO houseRequestDTO) {
        House house = HouseMapper.toEntity(houseRequestDTO);
        House savedHouse = houseRepository.save(house);
        return HouseMapper.toResponseDTO(savedHouse);
    }

    @Override
    public HouseResponseDTO getHouse(Long houseId) {
        House house = findHouseById(houseId);
        return HouseMapper.toResponseDTO(house);
    }

    @Override
    public List<HouseResponseDTO> getAllHouses() {
        List<House> houses = houseRepository.findAll();
        return HouseMapper.toResponseDTOList(houses);
    }

    @Override
    @Transactional
    public HouseResponseDTO updateHouse(Long houseId, HouseRequestDTO houseRequestDTO) {
        House house = findHouseById(houseId);
        HouseMapper.updateEntityFromDTO(house, houseRequestDTO);
        House updatedHouse = houseRepository.save(house);
        return HouseMapper.toResponseDTO(updatedHouse);
    }

    @Override
    @Transactional
    public void deleteHouse(Long houseId) {
        House house = findHouseById(houseId);
        houseRepository.delete(house);
    }

    @Override
    @Transactional
    public void assignUserToHouse(Long houseId, Long userId) {
        House house = findHouseById(houseId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setHouse(house);
        userRepository.save(user);
    }

    private House findHouseById(Long houseId) {
        return houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException("House not found with id: " + houseId));
    }
}