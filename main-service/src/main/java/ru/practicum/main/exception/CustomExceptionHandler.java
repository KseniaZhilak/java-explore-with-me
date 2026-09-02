package ru.practicum.main.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            BadRequestException.class
    })
    public ApiError badRequestException(Exception ex) {
        return buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError badRequestException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildApiError(HttpStatus.BAD_REQUEST, message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiError badRequestException(MethodArgumentTypeMismatchException ex) {
        return buildApiError(HttpStatus.BAD_REQUEST,
                "Unknown value of parameter " + ex.getName() + ": " + ex.getValue());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiError badRequestException(HttpMessageNotReadableException ex) {
        return buildApiError(HttpStatus.BAD_REQUEST,
                "Request body is malformed or contains an unknown value");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ApiError conflictException(ConflictException ex) {
        return buildApiError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError notFoundException(NotFoundException ex) {
        return buildApiError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ApiError forbiddenException(ForbiddenException ex) {
        return buildApiError(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    private ApiError buildApiError(HttpStatus status, String message) {
        return new ApiError(
                status.name(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now().format(TIMESTAMP_FORMATTER)
        );
    }
}
