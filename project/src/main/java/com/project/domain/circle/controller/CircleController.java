package com.project.domain.circle.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.circle.api.CircleService;
import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "circle", description = "그룹 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/circle")
public class CircleController {

    private final CircleService circleService;

    @Operation(summary = "그룹 생성 API", description = "그룹을 생성한다.", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "그룹 생성 성공",
                    content = @Content(schema = @Schema(implementation = CircleDTO.CircleSimpleInfoResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "그룹 생성 실패",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> createCircle(@AuthUser Users user, @Valid @RequestBody CircleDTO.CreateCircleRequest createCircleRequest) throws Exception {
        CircleDTO.CircleSimpleInfoResponse response = circleService.createCircle(user, createCircleRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "유저별 그룹 조회 API", description = "유저별로 그룹을 조회한다.", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "유저별 그룹 조회 성공",
                    content = @Content(schema = @Schema(implementation = CircleDTO.CircleSimpleInfoListResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "유저별 그룹 조회 실패",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("")
    @Permission()
    private ResponseEntity<CircleDTO.CircleSimpleInfoListResponse> getAllCircleByUser(@AuthUser Users user) {
        CircleDTO.CircleSimpleInfoListResponse response = circleService.getAllCircleByUser(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "그룹별 유저 조회", description = "그룹별 유저 리스트를 조회한다.", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "그룹별 유저 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = CircleDTO.CircleWithJoinUserResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "그룹별 유저 리스트 조회 실패",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{circleId}/users")
    @Permission
    private ResponseEntity<CircleDTO.CircleWithJoinUserResponse> getUserListByCircle(
            @Parameter(description = "circle 의 id")
            @PathVariable Long circleId) throws Exception {
        CircleDTO.CircleWithJoinUserResponse response = circleService.getJoinedUserOfCircle(circleId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 그룹 상세 조회
    @GetMapping("/{circleId}")
    @Permission
    private ResponseEntity<CircleDTO.CircleDetailInfoResponse> getCircle(@PathVariable Long circleId) throws Exception {
        CircleDTO.CircleDetailInfoResponse response = circleService.getCircleDetail(circleId);

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

    // 유저가 링크를 타고 들어올 경우, 링크값으로 써클을 찾고, 로그인을 완료했을 경우 그룹 초대를 하도록 한다.
    @PostMapping("/invite-key/{circleKey}")
    @Permission
    private ResponseEntity<CircleDTO.InviteUserFromLinkResponse> inviteCircleFromLink(@AuthUser Users user, @PathVariable("circleKey") String circleKey) {
        CircleDTO.InviteUserFromLinkResponse response = circleService.inviteUserFromLink(user, circleKey);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹 초대를 수락한다.
    @PostMapping("/{circleId}/accept")
    @Permission
    private ResponseEntity<CircleDTO.acceptCircleInvitationResponse> acceptCircleInvitation(@AuthUser Users user, @PathVariable Long circleId) {
        CircleDTO.acceptCircleInvitationResponse response = circleService.acceptCircleInvitation(user, circleId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹이름을 수정한다.
    @PatchMapping("/{circleId}")
    @Permission
    private ResponseEntity<CircleDTO.CircleSimpleInfoResponse> updateCircle(@AuthUser Users user, @PathVariable Long circleId,
                                                                            @Valid @RequestPart(required = false) CircleDTO.UpdateCircleRequest request, @RequestPart(required = false) MultipartFile picture) {
        CircleDTO.CircleSimpleInfoResponse response = circleService.updateCircle(user, circleId, request, picture);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 방장인 유저가 방장 권한을 위임한다.
    @PatchMapping("/{circleId}/master")
    @Permission
    private ResponseEntity<CircleDTO.CircleWithJoinUserResponse> updateCircleMaster(@AuthUser Users user, @PathVariable Long circleId, @RequestBody CircleDTO.UpdateCircleMasterRequest request) {
        CircleDTO.CircleWithJoinUserResponse response = circleService.updateCircleMaster(user, circleId, request.getUserId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 그룹별로 아직 초대를 수락하지 않은 유저들 조회
    @GetMapping("/{circleId}/not-accept-invite")
    @Permission
    private ResponseEntity<CircleDTO.NotAcceptCircleInviteUserResponse> getAllNotAcceptInviteUser(@AuthUser Users user, @PathVariable Long circleId) {
        CircleDTO.NotAcceptCircleInviteUserResponse response = circleService.getAllNotAcceptCircleInviteUser(user, circleId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 아직 수락하지 않았을 경우 초대를 취소한다,
    @PatchMapping("/{circleId}/cancel-invite-user/{cancelUserId}")
    @Permission
    private ResponseEntity<CircleDTO.cancelInviteCircleResponse> cancelCircleInvitation(
            @AuthUser Users user,
            @PathVariable Long circleId,
            @PathVariable Long cancelUserId
    ) {
        CircleDTO.cancelInviteCircleResponse response = circleService.cancelCircleInvitation(user, circleId, cancelUserId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}