// File: energy-optimization-backend/src/main/java/hpe/energy_optimization_backend/dto/ForwardRequestDTO.java
package hpe.energy_optimization_backend.dto;

import lombok.Data;
public class ForwardRequestDTO {
    private String query;
    private String role;


    public String getQuery() {
        return query;
    }

    public String getRole() {
        return role;
    }
}