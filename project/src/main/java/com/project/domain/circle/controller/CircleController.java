package com.project.domain.circle.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.circle.api.CircleService;
import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.users.entity.Users;
import jakarta.validation.Valid;
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
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> createCircle(@AuthUser Users user, @Valid @RequestBody CircleDTO.CreateCircleRequest createCircleRequest) throws Exception {
        CircleDTO.CircleSimpleInfoResponse response = circleService.createCircle(user, createCircleRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저별로 그룹을 조회한다.
    @GetMapping("")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoListResponse> getCircleListByUser(@AuthUser Users user) throws Exception {
        CircleDTO.CircleSimpleInfoListResponse response = circleService.getCircleListByUser(user.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹을 선택해서 그룹원들을 조회한다.
    @GetMapping("/{circleId}/users")
    @Permission
    private ResponseEntity<CircleDTO.CircleWithJoinUserResponse> getUserListByCircle(@PathVariable Long circleId) throws Exception {
        CircleDTO.CircleWithJoinUserResponse response = circleService.getUserListByCircle(circleId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹에서 나온다.
    @DeleteMapping("/{circleId}")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> leaveCircle(@AuthUser Users user, @PathVariable Long circleId) throws Exception {
        CircleDTO.CircleSimpleInfoResponse response = circleService.leaveCircle(user.getId(), circleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //방장이 유저를 강퇴한다.
    @PostMapping("/{circleId}/expulsion")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> expulsionCircleFromCircle(@AuthUser Users user, @PathVariable Long circleId, @RequestBody CircleDTO.ExpulsionUserRequest request) {
        CircleDTO.CircleSimpleInfoResponse response = circleService.expulsionUserFromCircle(user.getId(), circleId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저를 그룹에 초대한다.
    @PostMapping("/{circleId}/invite")
    @Permission
    private ResponseEntity<CircleDTO.InviteCircleResponse> inviteCircle(@AuthUser Users user, @PathVariable Long circleId, @RequestBody @Valid CircleDTO.InviteCircleRequest request) throws Exception {
        CircleDTO.InviteCircleResponse response = circleService.inviteCircle(user, circleId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹 초대를 수락한다.
    @PostMapping("/{circleId}/allow")
    @Permission
    private ResponseEntity<CircleDTO.AllowJoinCircleResponse> allowJoinCircle(@AuthUser Users user, @PathVariable Long circleId) {
        CircleDTO.AllowJoinCircleResponse response = circleService.allowJoinCircle(user, circleId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 유저가 그룹이름을 수정한다.
    @PatchMapping("/{circleId}")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> updateCircle(@AuthUser Users user, @PathVariable Long circleId, @Valid @RequestBody CircleDTO.UpdateCircleRequest request) throws Exception {
        CircleDTO.CircleSimpleInfoResponse response = circleService.updateCircle(user.getId(), circleId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}