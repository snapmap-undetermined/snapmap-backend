package com.project.domain.users.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.users.api.interfaces.UserService;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.api.interfaces.AuthService;
import com.project.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저 API", description = "User Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserDTO.SignUpResponse.class)))})
    @Operation(summary = "회원가입", description = "회원가입을 한다.")
    @PostMapping("/signUp")
    private ResponseEntity<UserDTO.SignUpResponse> signUp(@Valid @RequestBody UserDTO.SignUpRequest signUpRequest) throws Exception {
        UserDTO.SignUpResponse response = authService.signUp(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserDTO.LoginResponse.class)))})
    @Operation(summary = "로그인", description = "로그인을 한다.")
    @PostMapping("/login")
    private ResponseEntity<UserDTO.LoginResponse> login(@Valid @RequestBody UserDTO.LoginRequest loginRequest) throws Exception {
        UserDTO.LoginResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserDTO.UserSimpleInfoResponse.class)))})
    @Operation(summary = "유저 조회", description = "검색을 통해서 유저를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("")
    @Permission
    private ResponseEntity<UserDTO.UserSimpleInfoResponse> getUserByNickname(@Parameter(description = "유저의 nickname") @RequestParam String userNickname) throws Exception {

        UserDTO.UserSimpleInfoResponse response = userService.getUserByNickname(userNickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserDTO.UserSimpleInfoResponse.class)))})
    @Operation(summary = "유저 수정", description = "유저의 정보를 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @Permission
    private ResponseEntity<UserDTO.UserSimpleInfoResponse> updateUser(@AuthUser Users user, @RequestBody UserDTO.UpdateUserRequest request) {
        UserDTO.UserSimpleInfoResponse response = userService.updateUser(user.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = void.class)))})
    @Operation(summary = "유저 탈퇴", description = "유저가 탈퇴한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @Permission
    private void deleteUser(@AuthUser Users user) {
        userService.deleteUser(user);
    }
}
