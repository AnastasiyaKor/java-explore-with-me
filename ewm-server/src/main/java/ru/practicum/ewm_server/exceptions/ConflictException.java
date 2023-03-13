package ru.practicum.ewm_server.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
