// File: energy-optimization-backend/src/main/java/hpe/energy_optimization_backend/controller/ForwardController.java
package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.dto.ForwardRequestDTO;
import hpe.energy_optimization_backend.service.FastApiService;
import hpe.energy_optimization_backend.urlMapper.AgentUrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ForwardController {

    @Autowired
    private FastApiService fastApiService;

    @PostMapping(AgentUrlMapper.AGENT_URL)
    public String forwardRequest(@RequestBody ForwardRequestDTO requestDTO) {
        String query = requestDTO.getQuery();
        String role = requestDTO.getRole();
        Long houseId = "HOUSE_OWNER".equals(role) ? 1L : null;
        return fastApiService.forwardRequest(query, houseId);
    }
}