// File: src/main/java/hpe/energy_optimization_backend/exception/houseAndDevice/UnauthorizedAccessException.java
package hpe.energy_optimization_backend.exception.user;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
