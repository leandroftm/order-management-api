package com.leandroftm.ordermanagement.order_management_api.exception;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.NotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.dto.ApiError;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, errors, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_CREDENTIALS, "Invalid credentials", request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getErrorCode(), ex.getMessage(), request);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(DomainException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNEXPECTED_ERROR, "Unexpected error", request);
    }

    //Build Api Error
    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            ErrorCode errorCode,
            String message,
            HttpServletRequest request) {
        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                errorCode,
                List.of(message),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(apiError);
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            ErrorCode errorCode,
            List<String> messages,
            HttpServletRequest request) {
        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                errorCode,
                messages,
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(apiError);
    }
}
