// File: energy-optimization-backend/src/main/java/hpe/energy_optimization_backend/dto/ForwardRequestDTO.java
package hpe.energy_optimization_backend.dto.request;
import lombok.Getter;

@Getter
public class ForwardRequestDTO {
    private String query;
    private String role;
}