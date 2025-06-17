package com.examcell.resultgen.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", "")); // Get request path

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Add handlers for other exceptions like ValidationException, AccessDeniedException etc.
    // @ExceptionHandler(MethodArgumentNotValidException.class) -> Handle validation errors from DTOs
    // @ExceptionHandler(AccessDeniedException.class) -> Handle authorization failures (e.g., prof marking wrong subject)

     @ExceptionHandler(IllegalArgumentException.class) // Example for handling bad requests
     public ResponseEntity<Object> handleIllegalArgumentException(
             IllegalArgumentException ex, WebRequest request) {

         Map<String, Object> body = new LinkedHashMap<>();
         body.put("timestamp", LocalDateTime.now());
         body.put("status", HttpStatus.BAD_REQUEST.value());
         body.put("error", "Bad Request");
         body.put("message", ex.getMessage());
         body.put("path", request.getDescription(false).replace("uri=", ""));

         return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
     }

    // Generic fallback handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(
            Exception ex, WebRequest request) {

        logger.error("An unexpected error occurred: ", ex); // Log the full stack trace

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred. Please contact support."); // Generic message for users
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

} 