package com.KokoSky.WeatherService.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> exceptionHandler(
            DuplicateResourceException ex,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );

        LOGGER.error(ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {

        // Extract field errors and format them into a map
        Map<String, String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField, // Field name
                        fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Validation error") // Fallback for null
                ));

        // Create a user-friendly error message
        String errorMessage = "Validation failed for the following fields: " +
                errors.keySet().stream()
                        .map(field -> field + " (" + errors.get(field) + ")")
                        .collect(Collectors.joining(", "));

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        LOGGER.error(errorMessage, exception);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        LOGGER.error(exception.getMessage(), exception);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ApiError> handleException(
            LocationNotFoundException exception,
            HttpServletRequest request
    ) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        LOGGER.error(exception.getMessage(), exception);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

}
