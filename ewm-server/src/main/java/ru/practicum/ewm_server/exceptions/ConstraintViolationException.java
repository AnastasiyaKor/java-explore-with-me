package ru.practicum.ewm_server.exceptions;

public class ConstraintViolationException extends RuntimeException {
    public ConstraintViolationException(String message) {
        super(message);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
