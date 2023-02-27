package com.project.domain.users.controller;

import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.api.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/")
public class UserController {

    private final AuthService authService;

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
}
