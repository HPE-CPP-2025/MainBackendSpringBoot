package hpe.energy_optimization_backend.exception.user;

public class UsernameAlreadyInUseException extends RuntimeException{
    public UsernameAlreadyInUseException(String message) {
        super(message);
    }
}
