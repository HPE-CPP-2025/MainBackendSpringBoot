package hpe.energy_optimization_backend.exception.token;

public class MissingRequestCookieException extends RuntimeException {
    public MissingRequestCookieException(String message) {
        super(message);
    }
}
