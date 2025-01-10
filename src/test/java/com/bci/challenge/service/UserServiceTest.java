package com.bci.challenge.service;


import com.bci.challenge.dto.LoginResponseDto;
import com.bci.challenge.dto.PhoneDto;
import com.bci.challenge.dto.UserDto;
import com.bci.challenge.dto.UserResponseDto;
import com.bci.challenge.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.xml.bind.ValidationException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldGetAuthenticationToken() throws ValidationException {
        UserDto userDto = getUserDto(false, "test@test1.com");
        UserResponseDto response = userService.createUser(userDto);

        assertNotNull("response shouldn't be null", response);
        assertNotNull("token shouldn't be null", response.getToken());
        assertTrue("isActive", response.isActive());
    }

    @Test
    public void shouldGetValidationExceptionUserAlreadyExist() throws ValidationException {
        UserDto userDto = getUserDto(false, "test@test1.com");
        UserResponseDto response = userService.createUser(userDto);

        Assertions.assertThrows(
                ValidationException.class,
                () -> {
                    userService.createUser(userDto);
                });
    }

    @Test
    public void shouldGetAuthenticationTokenWhenUserWithPhones() throws ValidationException {
        UserDto userDto = getUserDto(true, "test@test2.com");
        UserResponseDto response = userService.createUser(userDto);

        assertNotNull("response shouldn't be null", response);
        assertNotNull("token shouldn't be null", response.getToken());
        assertTrue("isActive", response.isActive());
    }

    @Test
    public void shouldGetCurrentUser() throws ValidationException {
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("test@test.com");
        when(authentication.isAuthenticated()).thenReturn(true);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserDto userDto = getUserDto(false, "test@test.com");
        UserResponseDto response = userService.createUser(userDto);

        LoginResponseDto loginResponseDto = userService.getCurrentUser();

        assertNotNull("response shouldn't be null", loginResponseDto);
        assertNotNull("token shouldn't be null", loginResponseDto.getToken());
        assertTrue("isActive", loginResponseDto.isActive());
        assertTrue("", "Test".equals(loginResponseDto.getName()));
        assertTrue("", "test@test.com".equals(loginResponseDto.getEmail()));
        assertTrue("", "password".equals(loginResponseDto.getPassword()));

    }

    private static UserDto getUserDto(boolean hasPhones, String email) {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword("password");
        userDto.setName("Test");

        if (hasPhones) {
            PhoneDto phoneDto = PhoneDto.builder()
                    .number(123)
                    .cityCode(3)
                    .countryCode("+57")
                    .build();
            userDto.setPhones(List.of((phoneDto)));
        }

        return userDto;
    }

}
