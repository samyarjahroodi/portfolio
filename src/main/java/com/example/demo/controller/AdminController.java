package com.example.demo.controller;

import com.example.demo.service.core.AdminService;
import com.example.demo.dto.requestDto.UsernameRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/deActive")
    public ResponseEntity<String> deActive(@RequestBody UsernameRequestDto usernameRequestDto) {
        try {
            if (adminService.deActiveUserOrAuthor(usernameRequestDto.getUsername())) {
                return ResponseEntity.ok("User " + usernameRequestDto.getUsername() + " has been deactivated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User " + usernameRequestDto.getUsername() + " not found");
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deactivating the user.");
        }
    }
}
