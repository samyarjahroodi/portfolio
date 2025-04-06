package com.example.demo.dto.requestDto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationRequestDto {
    private String username;

    private String password;

    private String email;

}
