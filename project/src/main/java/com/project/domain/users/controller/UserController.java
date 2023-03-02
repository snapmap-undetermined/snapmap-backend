package com.project.domain.users.controller;

import com.project.common.annotation.AuthUser;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.users.api.interfaces.UserService;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.api.interfaces.AuthService;
import com.project.domain.users.entity.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    // 회원가입
    @PostMapping("/signUp")
    private ResponseEntity<UserDTO.SignUpResponse> signUp(@Valid @RequestBody UserDTO.SignUpRequest signUpRequest) throws Exception{
        UserDTO.SignUpResponse response = authService.signUp(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
    private ResponseEntity<UserDTO.LoginResponse> login(@Valid @RequestBody UserDTO.LoginRequest loginRequest) throws Exception {
        UserDTO.LoginResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // user 조회(검색)
    @GetMapping("")
    private ResponseEntity<UserDTO.UserSimpleInfoResponse> getUserByNickname(@RequestParam String userNickname) throws Exception {

        UserDTO.UserSimpleInfoResponse response = userService.getUserByNickname(userNickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("")
    private ResponseEntity<UserDTO.UserSimpleInfoResponse> updateUserNickname(@AuthUser Users user, @RequestBody  UserDTO.UpdateUserRequest request) {
        UserDTO.UserSimpleInfoResponse response = userService.updateUser(user.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
