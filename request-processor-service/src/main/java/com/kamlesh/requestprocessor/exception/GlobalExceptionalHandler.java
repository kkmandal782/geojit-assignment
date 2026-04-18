package com.kamlesh.requestprocessor.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionalHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionalHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        log.warn("Validation failed for request: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    log.debug("Field error - {}: {}", error.getField(), error.getDefaultMessage());
                    errors.put(error.getField(), error.getDefaultMessage());
                });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(JobAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleJobAlreadyExists(JobAlreadyExistException ex) {

        log.warn("Duplicate processing job attempt: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Processing Job already exists");

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, String>> handleDateTimeParseException(DateTimeParseException ex) {

        log.warn("Invalid date format received: {}", ex.getParsedString());

        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Invalid Date Format");
        errors.put("message", "Please use format yyyy-MM-dd for " + ex.getParsedString());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Map<String, String>> handleDateTimeException(DateTimeException ex) {

        log.error("DateTime error occurred", ex);

        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Date error");
        errors.put("message", "Invalid date operation");

        return ResponseEntity.badRequest().body(errors);
    }
}