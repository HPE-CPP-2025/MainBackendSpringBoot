package hpe.energy_optimization_backend.urlMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FastApiUrlMapper {

    @Value("${fastapi.server.url}")
    private String fastApiServerUrl;

    public String getFastApiServerUrl() {
        return fastApiServerUrl;
    }
}