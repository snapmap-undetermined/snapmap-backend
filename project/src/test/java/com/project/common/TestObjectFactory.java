package com.project.common;

import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;

public class TestObjectFactory {

    public static Users createUser(Long id, String email, String nickname, String password) {
        return Users.builder().id(id).email(email).nickname(nickname).password(password).build();
    }

    public static UserDTO.SignUpRequest createSingUpRequest(String email, String nickname, String password) {
        return UserDTO.SignUpRequest.builder().email(email).nickname(nickname).password(password).build();
    }

    public static UserDTO.LoginRequest createLoginRequest(String email, String password) {
        return UserDTO.LoginRequest.builder().email(email).password(password).build();
    }

    public static UserDTO.EmailRequest createEmailRequest(String email) {
        return UserDTO.EmailRequest.builder().email(email).build();
    }

    public static UserDTO.UpdateUserRequest createUpdateUserRequest(String nickname, String profileImage) {
        return UserDTO.UpdateUserRequest.builder().nickname(nickname).profileImage(profileImage).build();
    }

    public static FriendDTO.CreateFriendRequest createFriendRequest(Long friendUserId) {
        return FriendDTO.CreateFriendRequest.builder().friendUserId(friendUserId).build();
    }
}
