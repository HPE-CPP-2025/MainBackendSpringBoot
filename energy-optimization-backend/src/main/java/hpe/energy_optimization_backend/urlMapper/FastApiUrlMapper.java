package hpe.energy_optimization_backend.urlMapper;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FastApiUrlMapper {

    @Value("${fastapi.server.url}")
    private String fastApiServerUrl;

}