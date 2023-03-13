package ru.practicum.ewm_server.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.error(e.getMessage(), e);
        return new ApiError(
                HttpStatus.CONFLICT,
                "Нарушение целостности данных",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                "Запрос составлен некорректно",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                "Запрос составлен некорректно",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return new ApiError(
                HttpStatus.BAD_REQUEST,
                "Запрос составлен некорректно",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ApiError(
                HttpStatus.NOT_FOUND,
                "Не найдено",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleSQLException(final SQLException e) {
        log.error(e.getMessage(), e);
        return new ApiError(
                HttpStatus.CONFLICT,
                "Запрос составлен некорректно",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleThrowable(final Throwable e) {
        log.error(e.getMessage(), e);
        return new ApiError(
                HttpStatus.CONFLICT,
                "Запрос составлен некорректно",
                e.getMessage(),
                LocalDateTime.now()
        );
    }
}
