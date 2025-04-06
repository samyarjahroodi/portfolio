package com.example.demo.dto.ResponseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationResponseDto {
    private String username;

    private String email;

}
