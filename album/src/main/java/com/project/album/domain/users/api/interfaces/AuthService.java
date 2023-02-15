package com.project.album.domain.users.api.interfaces;

import com.project.album.domain.users.dto.UserDTO;

public interface AuthService {

    UserDTO.SignUpResponse signUp(UserDTO.SignUpRequest signUpRequest) throws Exception;

    UserDTO.LoginResponse login(UserDTO.LoginRequest loginRequest) throws Exception;

}
