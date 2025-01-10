package com.bci.challenge.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponseDto extends UserResponseDto {

    private String name;
    private String email;
    private String password;

    private List<PhoneDto> phones;


}
