package dev.sorokin.event.notificator.exception.kafka;

public class RetryableException extends RuntimeException {

    public RetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryableException(Throwable cause) {
        super(cause);
    }
}
