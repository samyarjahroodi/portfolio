package com.example.demo.service.core;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UsersRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.dto.requestDto.UserRegistrationRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User saveUser(UserRegistrationRequestDto userRegistrationRequestDto) {
        logger.info("Registering user with username; {}", userRegistrationRequestDto.getUsername());

        User user = userMapper.userDtoRegistrationRequestToUser(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        user.setRole(Role.USER);


        return usersRepository.save(user);
    }

    public ResponseEntity<Object> checkLogin(String username, String rawPassword) {
        User userById = usersRepository.findByUsername(username);

        if (userById != null && passwordEncoder.matches(rawPassword, userById.getPassword())) {
            String jwtToken = jwtUtil.generateToken(userById.getUsername(), userById.getRole());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", jwtToken);
            response.put("role",userById.getRole());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
