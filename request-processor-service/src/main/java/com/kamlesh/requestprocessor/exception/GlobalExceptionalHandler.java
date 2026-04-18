package com.kamlesh.requestprocessor.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionalHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionalHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        System.out.println(">>> Caught MethodArgumentNotValidException");

        Map<String , String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error->errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(JobAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistException(JobAlreadyExistException ex){
        Map<String , String> errors = new HashMap<>();

        log.warn("Processing Job already exist {}",ex.getMessage());

        errors.put("message","Processing Job already exists");

       return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Map<String, String>> handleDateTimeParseException(DateTimeParseException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("error","Invalid Date Format");
        errors.put("message", "Please use the correct date format: '    yyyy-MM-dd'"+" for "+ex.getParsedString());
        return ResponseEntity.badRequest().body(errors);
    }


}
