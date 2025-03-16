package hpe.energy_optimization_backend.service.Impl;

import hpe.energy_optimization_backend.service.FastApiService;
import hpe.energy_optimization_backend.urlMapper.UrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FastApiServiceImpl implements FastApiService {

    @Autowired
    private UrlMapper urlMapper;

    @Override
    public String forwardRequest(String query, Long homeId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = urlMapper.getFastApiServerUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);
        if (homeId != null) {
            requestBody.put("house_id", homeId.toString());
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(url, request, String.class);
    }
}