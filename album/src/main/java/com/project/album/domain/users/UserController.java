package com.project.album.domain.users;

import com.project.album.domain.users.dto.UserDTO;
import com.project.album.domain.users.api.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/")
public class UserController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signUp")
    private ResponseEntity<UserDTO.SignUpResponse> signUp(@RequestBody UserDTO.SignUpRequest signUpRequest) throws Exception {
        UserDTO.SignUpResponse response = authService.signUp(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
    private ResponseEntity<UserDTO.LoginResponse> login(@RequestBody UserDTO.LoginRequest loginRequest) throws Exception {
        UserDTO.LoginResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
