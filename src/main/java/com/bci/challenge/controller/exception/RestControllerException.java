package com.bci.challenge.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestControllerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<Object, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<Object, Object> errors = new HashMap<>();
        Map<Object, Object> result = new HashMap<>();
        List<Object> data = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put("detail", error.getDefaultMessage());
            errors.put("timestamp", LocalDateTime.now());
            errors.put("code", HttpStatus.BAD_REQUEST.value());
            data.add(errors);
        }
        result.put("error", data);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}