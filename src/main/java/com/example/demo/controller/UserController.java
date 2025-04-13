package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.core.UserService;
import com.example.demo.dto.ResponseDto.UserRegistrationResponseDto;
import com.example.demo.dto.requestDto.UserLoginRequestDto;
import com.example.demo.dto.requestDto.UserRegistrationRequestDto;
import com.example.demo.service.core.VerificationTokenService;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto
            , HttpSession session) {
        String expectedCaptcha = (String) session.getAttribute("captcha");
        if (!userRegistrationRequestDto.getCaptcha().equalsIgnoreCase(expectedCaptcha)) {
            return ResponseEntity.badRequest().body("Invalid Captcha.");
        }

        User savedUser = userService.saveUser(userRegistrationRequestDto);
        UserRegistrationResponseDto userRegistrationResponseDto = userMapper.userToUserRegistrationResponseDto(savedUser);
        return ResponseEntity.ok(userRegistrationResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpSession httpSession) {
        String expectedCaptcha = (String) httpSession.getAttribute("captcha");
        if (!userLoginRequestDto.getCaptcha().equalsIgnoreCase(expectedCaptcha)) {
            return ResponseEntity.badRequest().body("Invalid Captcha.");
        }
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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        userService.completePasswordReset(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }


}
