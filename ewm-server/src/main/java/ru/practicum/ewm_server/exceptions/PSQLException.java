package ru.practicum.ewm_server.exceptions;

public class PSQLException extends RuntimeException {
    public PSQLException(String message) {
        super(message);
    }

    public String getMassage() {
        return super.getMessage();
    }
}
