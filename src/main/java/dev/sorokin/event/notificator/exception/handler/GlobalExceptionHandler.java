package dev.sorokin.event.notificator.exception.handler;

import dev.sorokin.event.notificator.api.dto.ErrorMessageDto;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> handleCommonException(Exception e) {
        LOGGER.error("Handle common exception", e);

        var error = new ErrorMessageDto(
                "Internal Server error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorMessageDto> handleJwtException(JwtException e) {
        LOGGER.error("Handle jwt exception", e);

        var error = new ErrorMessageDto(
                "Invalid Jwt Token",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDto> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error("Handle illegal argument exception", e);

        var error = new ErrorMessageDto(
                "Illegal argument error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(value = {AuthenticationException.class, SecurityException.class})
    public ResponseEntity<ErrorMessageDto> handleAuthenticationException(Exception e) {
        LOGGER.error("Handle authentication(security) exception", e);

        ErrorMessageDto error = new ErrorMessageDto(
                "Authentication error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorMessageDto> handleEntityNotFoundException(Exception e) {
        LOGGER.error("Handle not found exception", e);

        ErrorMessageDto error = new ErrorMessageDto(
                "Not Found Exception",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}