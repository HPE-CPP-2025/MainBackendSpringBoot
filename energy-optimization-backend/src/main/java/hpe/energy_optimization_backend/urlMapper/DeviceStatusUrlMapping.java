package hpe.energy_optimization_backend.urlMapper;

public final class DeviceStatusUrlMapping {
    private DeviceStatusUrlMapping() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BASE_API = "/api/device-status";
    public static final String TOGGLE_DEVICE = BASE_API + "/{deviceId}/toggle";
    public static final String SUBSCRIBE_HOUSE = BASE_API + "/house/{houseId}/subscribe";
    public static final String GET_HOUSE_DEVICE_STATUS = BASE_API + "/house/{houseId}/status";
}