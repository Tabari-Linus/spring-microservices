package com.example.orderservice.exception;

import com.example.orderservice.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity
                .status(404)
                .body(new ErrorResponse(e.getMessage(), "NOT_FOUND", 404));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity
                .status(404)
                .body(new ErrorResponse(e.getMessage(), "NOT_FOUND", 404));
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("An unexpected error occurred: " + e.getMessage(), "INTERNAL_SERVER_ERROR", 500));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException e) {
        return ResponseEntity
                .status(e.getStatusCode().value())
                .body(new ErrorResponse("HTTP Client Error: " + e.getMessage(), "CLIENT_ERROR", e.getStatusCode().value()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("Runtime Exception: " + e.getMessage(), "RUNTIME_ERROR", 500));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(400)
                .body(new ErrorResponse("Invalid Argument: " + e.getMessage(), "BAD_REQUEST", 400));
    }
}
