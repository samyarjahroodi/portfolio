package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.core.UserService;
import com.example.demo.dto.ResponseDto.UserRegistrationResponseDto;
import com.example.demo.dto.requestDto.UserLoginRequestDto;
import com.example.demo.dto.requestDto.UserRegistrationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> createUser(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
        User savedUser = userService.saveUser(userRegistrationRequestDto);
        UserRegistrationResponseDto userRegistrationResponseDto = userMapper.userToUserRegistrationResponseDto(savedUser);
        return ResponseEntity.ok(userRegistrationResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        return userService.checkLogin(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword());
    }


}
