package com.bci.challenge.controller;

import com.bci.challenge.dto.LoginResponseDto;
import com.bci.challenge.dto.UserDto;
import com.bci.challenge.dto.UserResponseDto;
import com.bci.challenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@Valid @RequestBody UserDto userDto) {

        try {
            UserResponseDto response = userService.createUser(userDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ValidationException exc) {
            return new ResponseEntity<>(buildErrorResponse(exc), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login")
    public LoginResponseDto login() {
        return userService.getCurrentUser();
    }

    private static Map<Object, Object> buildErrorResponse(ValidationException exc) {
        Map<Object, Object> errors = new HashMap<>();
        Map<Object, Object> result = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("code", HttpStatus.BAD_REQUEST.value());
        errors.put("detail", exc.getMessage());
        result.put("error", List.of(errors));
        return result;
    }
}
