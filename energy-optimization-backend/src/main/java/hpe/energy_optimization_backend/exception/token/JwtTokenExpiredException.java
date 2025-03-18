// File: backend/src/main/java/com/dbms/mentalhealth/exception/JwtTokenExpiredException.java
package hpe.energy_optimization_backend.exception.token;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}