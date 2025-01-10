package com.bci.challenge.service.impl;

import com.bci.challenge.dto.LoginResponseDto;
import com.bci.challenge.dto.PhoneDto;
import com.bci.challenge.dto.UserDto;
import com.bci.challenge.dto.UserResponseDto;
import com.bci.challenge.model.Phone;
import com.bci.challenge.model.User;
import com.bci.challenge.repository.UserRepository;
import com.bci.challenge.service.UserService;
import com.bci.challenge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public UserResponseDto createUser(UserDto userDto) throws ValidationException {

        User user = userRepository.findByEmail(userDto.getEmail());

        if(user != null) {
            throw new ValidationException("User already exists");
        }

        user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        List<Phone> phones = new ArrayList<>();

        User currentUser = user;
        if (userDto.getPhones() != null && !userDto.getPhones().isEmpty()) {
            phones = userDto.getPhones().stream()
                    .map(p -> {
                        Phone phone = new Phone();
                        phone.setNumber(p.getNumber());
                        phone.setCityCode(p.getCityCode());
                        phone.setCountryCode(p.getCountryCode());
                        phone.setUser(currentUser);
                        return phone;
                    }).collect(Collectors.toList());
        }

        user.setPhones(phones);
        user.setCreationDate(LocalDateTime.now());
        user.setActive(true);
        user = userRepository.save(user);

        UserResponseDto response = new UserResponseDto();
        response.setId(String.valueOf(user.getId()));
        response.setCreated(user.getCreationDate());
        response.setLastLogin(LocalDateTime.now());

        response.setToken(jwtUtil.generateToken(user.getEmail()));
        response.setActive(user.isActive());
        return response;
    }

    @Override
    public LoginResponseDto getCurrentUser() {
        LoginResponseDto response = new LoginResponseDto();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User currentUser = userRepository.findByEmail(userName);

        List<PhoneDto> phones = List.of();
        if (currentUser.getPhones() != null && !currentUser.getPhones().isEmpty()) {
            phones = currentUser.getPhones().stream()
                    .map(p -> PhoneDto.builder()
                            .number(p.getNumber())
                            .cityCode(p.getCityCode())
                            .countryCode(p.getCountryCode()).build()).collect(Collectors.toList());
        }


        response.setToken(jwtUtil.generateToken(userName));
        response.setId(String.valueOf(currentUser.getId()));
        response.setCreated(currentUser.getCreationDate());
        response.setLastLogin(LocalDateTime.now());
        response.setActive(currentUser.isActive());

        response.setName(currentUser.getName());
        response.setEmail(currentUser.getEmail());
        response.setPassword(currentUser.getPassword());

        response.setPhones(phones);

        return response;
    }
}
