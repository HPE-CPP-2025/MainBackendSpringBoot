package hpe.energy_optimization_backend.controller;
import hpe.energy_optimization_backend.dto.request.ForwardRequestDTO;
import hpe.energy_optimization_backend.service.FastApiService;
import hpe.energy_optimization_backend.urlMapper.AgentUrlMapper;
import org.springframework.web.bind.annotation.*;

@RestController
public class ForwardController {

    private final FastApiService fastApiService;

    public ForwardController(FastApiService fastApiService) {
        this.fastApiService = fastApiService;
    }

    @PostMapping(AgentUrlMapper.AGENT_URL)
    public String forwardRequest(@RequestBody ForwardRequestDTO requestDTO) {
        String query = requestDTO.getQuery();
        String role = requestDTO.getRole();
        Long houseId = "HOUSE_OWNER".equals(role) ? 1L : null;
        return fastApiService.forwardRequest(query, houseId);
    }

}