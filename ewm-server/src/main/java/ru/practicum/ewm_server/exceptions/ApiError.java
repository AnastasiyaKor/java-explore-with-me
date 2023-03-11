package ru.practicum.ewm_server.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm_server.enums.StatusException;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiError {
    private final StatusException status;
    private final String reason;
    private final String message;
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private final LocalDateTime timestamp;

}
