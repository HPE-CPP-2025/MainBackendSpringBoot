package hpe.energy_optimization_backend.controller;

import hpe.energy_optimization_backend.service.FastApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ForwardController {

    @Autowired
    private FastApiService fastApiService;

    @PostMapping("/forward")
    public String forwardRequest(@RequestParam String query) {
        String role = "HOUSE_OWNER";
        Long houseId = "HOUSE_OWNER".equals(role) ? 1L : null;
        return fastApiService.forwardRequest(query, houseId);
    }
}