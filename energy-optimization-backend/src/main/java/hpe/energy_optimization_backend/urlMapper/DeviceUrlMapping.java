// File: src/main/java/hpe/energy_optimization_backend/urlMapper/DeviceUrlMapping.java
package hpe.energy_optimization_backend.urlMapper;

public final class DeviceUrlMapping {
    private DeviceUrlMapping() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BASE_API = "/api/devices";
    public static final String GET_ALL = BASE_API;
    public static final String GET_BY_ID = BASE_API + "/{id}";
    public static final String CREATE = BASE_API;
    public static final String UPDATE = BASE_API + "/{id}";
    public static final String DELETE = BASE_API + "/{id}";
}