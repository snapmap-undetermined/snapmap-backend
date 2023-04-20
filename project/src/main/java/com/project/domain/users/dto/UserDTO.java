package com.project.domain.users.dto;

import com.project.domain.users.entity.Users;
import com.project.common.entity.Role;
import com.project.common.message.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public class UserDTO {
    @Data
    public static class SignUpRequest {
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호는 8자리 이상이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
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
                    .activated(true)
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
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
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

    @Data
    public static class UserSimpleInfoResponse {
        private Long userId;
        private String userNickname;
        private String userProfileImage;

        public UserSimpleInfoResponse(Users user) {
            this.userId = user.getId();
            this.userNickname = user.getNickname();
            this.userProfileImage = user.getProfileImage();
        }
    }

    @Data
    public static class UpdateUserRequest{
        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;
        private String profileImage;
    }

    @Data
    public static class EmailRequest {

        @Email
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
    }

    @Data
    public static class EmailValidateCodeRequest {

        @NotBlank(message = "인증번호를 입력해주세요")
        private String authEmailKey;
    }

}
