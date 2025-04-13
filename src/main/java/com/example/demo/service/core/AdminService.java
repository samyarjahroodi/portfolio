package com.example.demo.service.core;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    public boolean deActiveUserOrAuthor(String username) {
        try {
            User user = userRepository.findByUsername(username).
                    orElseThrow(() -> new RuntimeException("Username not Found"));
            user.setDeActive(true);
            userRepository.save(user);
            logger.info("User {} has been deactivated successfully.", username);
            return true;
        } catch (Exception e) {
            logger.error("Error occurred while deactivating user {}: {}", username, e.getMessage());
            return false;
        }
    }

    public boolean activeAuthor(String username) {
        try {
            User user = userRepository.findByUsername(username).
                    orElseThrow(() -> new RuntimeException("Username not Found"));

            user.setDeActive(false);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            logger.error("Error occurred while activating user {}: {}", username, e.getMessage());
            return false;
        }


    }
}
