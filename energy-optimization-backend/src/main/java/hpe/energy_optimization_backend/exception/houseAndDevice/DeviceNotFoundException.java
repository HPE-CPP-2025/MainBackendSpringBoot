// File: src/main/java/hpe/energy_optimization_backend/exception/houseAndDevice/DeviceNotFoundException.java
package hpe.energy_optimization_backend.exception.houseAndDevice;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(String message) {
        super(message);
    }
}