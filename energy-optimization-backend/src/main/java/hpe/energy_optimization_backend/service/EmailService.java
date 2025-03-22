package hpe.energy_optimization_backend.service;

import hpe.energy_optimization_backend.dto.request.UserLoginRequestDTO;

public interface EmailService {
    void sendPasswordResetEmail(String email, String code);
}