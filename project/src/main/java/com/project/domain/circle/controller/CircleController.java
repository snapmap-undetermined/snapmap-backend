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
import org.springframework.web.multipart.MultipartFile;


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
    private ResponseEntity<CircleDTO.CircleSimpleInfoListResponse> getAllCircleByUser(@AuthUser Users user) {
        CircleDTO.CircleSimpleInfoListResponse response = circleService.getAllCircleByUser(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹을 선택해서 그룹원들을 조회한다.
    @GetMapping("/{circleId}/users")
    @Permission
    private ResponseEntity<CircleDTO.CircleWithJoinUserResponse> getUserListByCircle(@PathVariable Long circleId) throws Exception {
        CircleDTO.CircleWithJoinUserResponse response = circleService.getJoinedUserOfCircle(circleId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 그룹 상세 조회
    @GetMapping("/{circleId}")
    @Permission
    private ResponseEntity<CircleDTO.CircleDetailInfoResponse> getCircle(@PathVariable Long circleId) throws Exception {
        CircleDTO.CircleDetailInfoResponse response = circleService.getCircle(circleId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //유저가 그룹에서 나온다.
    @PatchMapping("/{circleId}/leave")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> leaveCircle(@AuthUser Users user, @PathVariable Long circleId) throws Exception {
        CircleDTO.CircleSimpleInfoResponse response = circleService.leaveCircle(user, circleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //방장이 유저를 강퇴한다.
    @PostMapping("/{circleId}/ban")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> banUserFromCircle(@AuthUser Users user, @PathVariable Long circleId, @RequestBody CircleDTO.BanUserRequest banUserRequest) {
        CircleDTO.CircleSimpleInfoResponse response = circleService.banUserFromCircle(user, circleId, banUserRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저를 그룹에 초대한다.
    @PostMapping("/{circleId}/invite")
    @Permission
    private ResponseEntity<CircleDTO.InviteUserResponse> inviteCircle(@AuthUser Users user, @PathVariable Long circleId, @RequestBody @Valid CircleDTO.InviteUserRequest request) {
        CircleDTO.InviteUserResponse response = circleService.inviteUser(user, circleId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹 초대를 수락한다.
    @PostMapping("/{circleId}/allow")
    @Permission
    private ResponseEntity<CircleDTO.AllowUserJoinResponse> allowUserJoin(@AuthUser Users user, @PathVariable Long circleId) {
        CircleDTO.AllowUserJoinResponse response = circleService.allowUserJoin(user, circleId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 유저가 그룹이름을 수정한다.
    @PatchMapping("/{circleId}")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> updateCircle(@AuthUser Users user, @PathVariable Long circleId,
                                                                            @Valid @RequestPart CircleDTO.UpdateCircleRequest request,
                                                                            @RequestPart MultipartFile picture) {
        CircleDTO.CircleSimpleInfoResponse response = circleService.updateCircle(user, circleId, request, picture);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}