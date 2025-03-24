package hpe.energy_optimization_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseResponseDTO {
    private Long houseId;
    private String houseName;
    private String location;
}