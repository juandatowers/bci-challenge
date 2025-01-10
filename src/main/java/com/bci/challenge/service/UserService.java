package com.bci.challenge.service;

import com.bci.challenge.dto.LoginResponseDto;
import com.bci.challenge.dto.UserDto;
import com.bci.challenge.dto.UserResponseDto;

import javax.xml.bind.ValidationException;

public interface UserService {
    UserResponseDto createUser(UserDto userDto) throws ValidationException;
    LoginResponseDto getCurrentUser();
}
