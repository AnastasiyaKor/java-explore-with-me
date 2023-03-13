package ru.practicum.ewm_server.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiError {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private final LocalDateTime timestamp;

}
