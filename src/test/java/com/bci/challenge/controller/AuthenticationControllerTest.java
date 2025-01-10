package com.bci.challenge.controller;


import com.bci.challenge.dto.LoginResponseDto;
import com.bci.challenge.dto.PhoneDto;
import com.bci.challenge.dto.UserDto;
import com.bci.challenge.dto.UserResponseDto;
import com.bci.challenge.service.UserService;
import com.bci.challenge.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {AuthenticationController.class, JwtUtil.class})
public class AuthenticationControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private final static String content = "{\n" +
            "    \"name\" : \"Juan Torres\",\n" +
            "    \"email\": \"juan.torres@gmail.com\",\n" +
            "    \"password\": \"Xyzab34qr\",\n" +
            "    \"phones\": [\n" +
            "        {\n" +
            "            \"number\": 12345,\n" +
            "            \"cityCode\": 3,\n" +
            "            \"countryCode\":\"+57\"   \n" +
            "        }\n" +
            "    ]\n" +
            "\n" +
            "}";

    @BeforeEach
    public void setUp() {
        Mockito.reset(userService);
    }

    @Test
    public void shouldSignUp() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");
        userDto.setName("Test");

        PhoneDto phoneDto = PhoneDto.builder()
                .number(123)
                .cityCode(3)
                .countryCode("+57")
                .build();
        userDto.setPhones(List.of((phoneDto)));

        UserResponseDto response = new UserResponseDto();
        response.setId("1");
        response.setCreated(LocalDateTime.now());
        response.setLastLogin(LocalDateTime.now());
        response.setToken("token");
        response.setActive(true);

        when(userService.createUser(userDto)).thenReturn(response);

        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());

    }


    @Test
    public void shouldReturnForbiddenWhenNoHeader() throws Exception {

        mockMvc.perform(get("/login")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldLoginWhenJwtToken() throws Exception {

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("test@test.com");
        when(authentication.isAuthenticated()).thenReturn(true);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        LoginResponseDto loginResponseDto =  userService.getCurrentUser();

        when(userService.getCurrentUser()).thenReturn(loginResponseDto);


        String token = jwtUtil.generateToken("test@test.com");
        mockMvc.perform(get("/login")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
