// File: src/main/java/hpe/energy_optimization_backend/exception/houseAndDevice/HouseNotFoundException.java
package hpe.energy_optimization_backend.exception.houseAndDevice;

public class HouseNotFoundException extends RuntimeException {
    public HouseNotFoundException(String message) {
        super(message);
    }
}