package ru.practicum.ewm_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm_server.enums.StatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        return new ApiError(
                StatusException.CONFLICT,
                "Нарушение целостности данных",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        return new ApiError(
                StatusException.BAD_REQUEST,
                "Запрос составлен некорректно",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ApiError(
                StatusException.BAD_REQUEST,
                "Запрос составлен некорректно",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return new ApiError(

                StatusException.NOT_FOUND,
                "Не найдено",
                e.getMessage(),

                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePSQLException(final PSQLException e) {
        return new ApiError(
                StatusException.CONFLICT,
                "Запрос составлен некорректно",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        return new ApiError(
                StatusException.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера",
                e.getMessage(),
                LocalDateTime.now()
        );
    }*/
}
