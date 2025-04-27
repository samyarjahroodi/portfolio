package com.example.demo.dto.requestDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationRequestDto {
    private String username;

    private String password;

    private String email;

    private String captcha;

}
