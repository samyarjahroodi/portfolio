package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.example.demo.dto.ResponseDto.UserRegistrationResponseDto;
import com.example.demo.dto.requestDto.UserRegistrationRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoRegistrationRequestToUser(UserRegistrationRequestDto userRegistrationRequestDto);

    UserRegistrationResponseDto userToUserRegistrationResponseDto(User user);
}
