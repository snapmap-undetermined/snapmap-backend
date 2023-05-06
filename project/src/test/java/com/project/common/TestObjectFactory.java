package com.project.common;

import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;

public class TestObjectFactory {

    public static UserDTO.SignUpRequest createSingUpRequest(String email, String nickname, String password) {
        return UserDTO.SignUpRequest.builder().email(email).nickname(nickname).password(password).build();
    }

    public static UserDTO.LoginRequest createLoginRequest(String email, String password) {
        return UserDTO.LoginRequest.builder().email(email).password(password).build();
    }

    public static UserDTO.EmailRequest createEmailRequest(String email) {
        return UserDTO.EmailRequest.builder().email(email).build();
    }
}
