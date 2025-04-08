package hpe.energy_optimization_backend.urlMapper;

public final class ApiKeyUrlMapping {
    private ApiKeyUrlMapping() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BASE_API = "/api/admin/api-keys";
    public static final String GENERATE_API_KEY = BASE_API;
    public static final String GET_ALL_API_KEYS = BASE_API;
    public static final String TOGGLE_API_KEY_STATUS = BASE_API + "/{id}/status";
    public static final String GET_API_KEY_DETAILS = BASE_API + "/{id}/details";
    public static final String DELETE_API_KEY = BASE_API + "/{id}";
}