package com.project.album.common.domain.users.dto;

import com.project.album.common.domain.users.entity.Users;
import com.project.album.common.entity.Role;
import com.project.album.common.message.Message;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class UserDTO {
    @Data
    public static class SignUpRequest {
        @NotBlank(message = Message.SIGN_UP_ID_MESSAGE)
        private String email;

        @NotBlank(message = Message.SIGN_UP_PASSWD_MESSAGE)
        private String password;

        @NotBlank(message = Message.SIGN_UP_NICKNAME_MESSAGE)
        private String nickname;

        private Role role = Role.USER;

        public Users toEntity(){
            return Users.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .role(role)
                    .build();
        }
    }

    @Data
    public static class SignUpResponse {
        private Long userId;
        private String email;
        private String nickname;
        private String accessToken;
        private String refreshToken;

        public SignUpResponse(Users user, String accessToken, String refreshToken) {
            this.userId = user.getId();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class LoginResponse{
        private Long userId;
        private String email;
        private String nickname;
        private String accessToken;
        private String refreshToken;

        public LoginResponse(Users user, String accessToken, String refreshToken) {
            this.userId = user.getId();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
