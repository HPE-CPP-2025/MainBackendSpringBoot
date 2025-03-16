package hpe.energy_optimization_backend.service;

public interface FastApiService {
    String forwardRequest(String query, Long house_id);
}