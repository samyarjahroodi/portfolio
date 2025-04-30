package com.example.demo.service.core;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserLogin;
import com.example.demo.entity.VerificationToken;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserLoginRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.dto.requestDto.UserRegistrationRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserLoginRepository userLoginRepository;

    private final JavaMailSender mailSender;


    @Value("${app.base-url}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public User saveUser(UserRegistrationRequestDto userRegistrationRequestDto) {

        if (checkIfUserExists(userRegistrationRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        logger.info("Registering user with username; {}", userRegistrationRequestDto.getUsername());

        User user = userMapper.userDtoRegistrationRequestToUser(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        user.setRole(Role.USER);

        VerificationToken verificationToken = tokenService.createVerificationToken(user);
        String token = verificationToken.getToken();
        emailService.SendVerificationEmail(user, token);

        UserLogin userLogin = new UserLogin();
        userLogin.setUser(user);
        userLogin.setLoginTime(LocalDateTime.now());
        userLoginRepository.save(userLogin);

        user.setUserLogin(Collections.singletonList(userLogin));

        return userRepository.save(user);
    }

    public User saveAuthor(UserRegistrationRequestDto userRegistrationRequestDto) {

        if (checkIfUserExists(userRegistrationRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        logger.info("Registering Author with username; {}", userRegistrationRequestDto.getUsername());

        User user = userMapper.userDtoRegistrationRequestToUser(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        user.setRole(Role.AUTHOR);

        VerificationToken verificationToken = tokenService.createVerificationToken(user);
        String token = verificationToken.getToken();
        emailService.SendVerificationEmail(user, token);

        UserLogin userLogin = new UserLogin();
        userLogin.setUser(user);
        userLogin.setLoginTime(LocalDateTime.now());
        user.setUserLogin(Collections.singletonList(userLogin));

        return userRepository.save(user);
    }

    private boolean checkIfUserExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public ResponseEntity<Object> checkLogin(String username, String rawPassword) {
        User userById = userRepository.findByUsername(username).
                orElseThrow(() -> new RuntimeException("User not found"));

        if (userById != null && passwordEncoder.matches(rawPassword, userById.getPassword())) {
            String jwtToken = jwtUtil.generateToken(userById.getUsername(), userById.getRole());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", jwtToken);
            response.put("role", userById.getRole());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


    public void enabledQualifiedUser(User user) {
        user.setEnabledByRegistration(true);
        userRepository.save(user);
    }

    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        sendPasswordResetEmail(user.getEmail(), token);
    }

    private void sendPasswordResetEmail(String email, String token) {
        String resetLink = baseUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink +
                "\nThis link will expire in 24 hours.");

        mailSender.send(message);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public void completePasswordReset(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
