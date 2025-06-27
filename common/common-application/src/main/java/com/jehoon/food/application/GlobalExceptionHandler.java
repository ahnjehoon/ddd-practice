package com.jehoon.food.application;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    public static final String UNEXPECTED_ERROR_MESSAGE = "예상치 못한 오류가 발생했습니다";
    public static final String VIOLATION_DELIMITER = " -- ";

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGenericException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return createErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_MESSAGE);
    }

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidationException(ValidationException validationException) {
        String errorMessage = extractErrorMessage(validationException);
        log.error(errorMessage, validationException);
        return createErrorDTO(HttpStatus.BAD_REQUEST, errorMessage);
    }

    private String extractErrorMessage(ValidationException validationException) {
        if (validationException instanceof ConstraintViolationException) {
            return ((ConstraintViolationException) validationException).getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(VIOLATION_DELIMITER));
        }
        return validationException.getMessage();
    }

    private ErrorDTO createErrorDTO(HttpStatus httpStatus, String message) {
        return ErrorDTO.builder()
                .code(httpStatus.getReasonPhrase())
                .message(message)
                .build();
    }
}
