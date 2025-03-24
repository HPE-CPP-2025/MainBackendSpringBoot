package hpe.energy_optimization_backend.exception;

import hpe.energy_optimization_backend.exception.houseAndDevice.DeviceNotFoundException;
import hpe.energy_optimization_backend.exception.token.JwtTokenExpiredException;
import hpe.energy_optimization_backend.exception.token.MissingRequestCookieException;
import hpe.energy_optimization_backend.exception.token.RefreshTokenException;
import hpe.energy_optimization_backend.exception.houseAndDevice.HouseNotFoundException;
import hpe.energy_optimization_backend.exception.user.*;
import jakarta.persistence.EmbeddedId;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex){
        return createErrorResponse(ex.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<String> handleDeviceNotFoundException(DeviceNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HouseNotFoundException.class)
    public ResponseEntity<String> handleHouseNotFoundException(HouseNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<String> handleUserNotActiveException(UserNotActiveException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<String> handleMissingRequestCookieException(MissingRequestCookieException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    public ResponseEntity<String> handleUsernameAlreadyInUseException(UsernameAlreadyInUseException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<String> handleInvalidUsernameException(InvalidUsernameException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<String> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<String> handleInvalidEmailException(InvalidEmailException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<String> handleRefreshTokenException(RefreshTokenException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserCredentialsException.class)
    public ResponseEntity<String> handleInvalidUserCredentialsException(InvalidUserCredentialsException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    // Authentication & Authorization Exceptions
    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<String> handleJwtTokenExpiredException(JwtTokenExpiredException ex) {
        return createErrorResponse("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    // Helper method to create consistent error responses
    private ResponseEntity<String> createErrorResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }
}