package com.project.domain.users.api.interfaces;

import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;

public interface UserService {

    UserDTO.UserSimpleInfoResponse getUserByNickname(String nickname);

    UserDTO.UserSimpleInfoResponse updateUser(Long userId, UserDTO.UpdateUserRequest request);

    void deleteUser(Users users);
}
