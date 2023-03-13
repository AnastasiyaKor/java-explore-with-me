package ru.practicum.ewm_server.exceptions;

public class SQLException extends RuntimeException {
    public SQLException(String message) {
        super(message);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
