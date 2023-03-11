package ru.practicum.ewm_server.exceptions;

public class MethodArgumentNotValidException extends RuntimeException {
    public MethodArgumentNotValidException(String message) {
        super(message);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
