package hpe.energy_optimization_backend.urlMapper;

public class UserUrlMapping {
    private UserUrlMapping() {
        throw new IllegalStateException("Utility class");
    }
    private static final String BASE_API = "/api/users";
    private static final String USER_ID_PATH = "/{userId}"; // Placeholder for userId

    public static final String USER_REGISTER = BASE_API + "/register"; // Register a new user
    public static final String USER_LOGIN = BASE_API + "/login"; // User login
    public static final String USER_LOGOUT = BASE_API + "/logout"; // User logout
    public static final String FORGOT_PASSWORD = BASE_API + "/forgot-password"; // Forgot password (trigger reset, no userId)
    public static final String RESET_PASSWORD = BASE_API + "/reset-password"; // Reset password (requires JWT for user verification)
    public static final String CHANGE_PASSWORD = BASE_API  + "/change-password";
}
