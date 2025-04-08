package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.core.UserService;
import com.example.demo.dto.ResponseDto.UserRegistrationResponseDto;
import com.example.demo.dto.requestDto.UserLoginRequestDto;
import com.example.demo.dto.requestDto.UserRegistrationRequestDto;
import com.example.demo.service.core.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VerificationTokenService tokenService;

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

    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String token) {
        VerificationToken verificationToken = tokenService.getVerificationToken(token);

        if (verificationToken == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        } else if (verificationToken.getExpiryDate().before(new Date())) {
            return ResponseEntity.badRequest().body("Token Expired");
        }
        userService.enabledQualifiedUser(verificationToken.getUser());

        return ResponseEntity.ok("Account Verified Successfully.");
    }


}
