package com.project.album.domain.circle.controller;

import com.project.album.common.annotation.AuthUser;
import com.project.album.domain.circle.api.CircleService;
import com.project.album.domain.circle.dto.CircleDTO;
import com.project.album.domain.usercircle.dto.UserCircleDTO;
import com.project.album.domain.users.dto.UserDTO;
import com.project.album.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/circle")
public class CircleController {

    private final CircleService circleService;

    //그룹을 생성한다.
    @PostMapping("")
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> createCircle(@AuthUser Users user, @RequestBody CircleDTO.CreateCircleRequest createCircleRequest) throws Exception {
        CircleDTO.CircleSimpleInfoResponse response = circleService.createCircle(user, createCircleRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저별로 그룹을 조회한다.
    @GetMapping("")
    private ResponseEntity<List<CircleDTO.CircleSimpleInfoResponse>> getCircleListByUser(@AuthUser Users user) throws Exception {
        List<CircleDTO.CircleSimpleInfoResponse> response = circleService.getCircleListByUser(user.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹을 선택해서 그룹원들을 조회한다.
    @GetMapping("/{circleId}/users")
    private ResponseEntity<List<UserDTO.UserSimpleInfoResponse>> getUserListByCircle(@AuthUser Users user, @PathVariable Long circleId) throws Exception {
        List<UserDTO.UserSimpleInfoResponse> response = circleService.getUserListByCircle(user.getId(), circleId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹에서 나온다.
    @DeleteMapping("/{circleId}")
    private int exitedUser(@AuthUser Users user, Long circleId) throws Exception {
        return circleService.exitedUser(user.getId(), circleId);
    }

    // 유저가 그룹에 조인한다.
    @PostMapping("/join")
    private ResponseEntity<UserCircleDTO.UserCircleSimpleInfoResponse> joinCircle(@AuthUser Users user, @RequestBody UserCircleDTO.JoinUserCircleRequest request) throws Exception {
        UserCircleDTO.UserCircleSimpleInfoResponse response = circleService.joinCircle(user, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹이름을 수정한다.
    @PatchMapping("/{circleId}")
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> updateCircleName(@PathVariable Long circleId, @RequestBody CircleDTO.UpdateCircleRequest request) throws Exception {
        CircleDTO.CircleSimpleInfoResponse response = circleService.updateCircleName(circleId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 초대를 해야한다.


}