package com.bci.challenge.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class UserDto {

    private String name;

    @NotBlank(message = "email is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email format: it should follow aaaaaaa@domain.something pattern"
    )
    private String email;

    @NotBlank(message = "password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z]{1})(?=(?:[^0-9]*[0-9]){2}[^0-9]*$)[a-zA-Z0-9]{8,12}$",
            message = "The password must have one uppercase letter, exactly two numbers, contain lowercase letters, and be between 8 and 12 characters long."
    )
    private String password;

    private List<PhoneDto> phones;
}
