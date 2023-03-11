package ru.practicum.ewm_server.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
