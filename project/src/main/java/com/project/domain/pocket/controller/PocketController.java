package com.project.domain.pocket.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.pocket.api.PocketService;
import com.project.domain.pocket.dto.PocketDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "포켓 API", description = "Pocket Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/pocket")
public class PocketController {

    private final PocketService pocketService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoResponse.class)))})
    @Operation(summary = "포켓 생성", description = "포켓을 생성한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> createPocket(@AuthUser Users user, @Valid @RequestBody PocketDTO.CreatePocketRequest createPocketRequest) throws Exception {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.createPocket(user, createPocketRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoListResponse.class)))})
    @Operation(summary = "포켓 리스트 조회", description = "자신이 속한 포켓 리스트를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoListResponse> getAllPocketByUser(@AuthUser Users user) {
        PocketDTO.PocketSimpleInfoListResponse response = pocketService.getAllPocketByUser(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketWithJoinUserResponse.class)))})
    @Operation(summary = "포켓에 속한 유저 리스트 조회", description = "포켓에 속한 유저 리스트를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pocketId}/users")
    @Permission
    private ResponseEntity<PocketDTO.PocketWithJoinUserResponse> getUserListByPocket(@Parameter(description = "포켓의 id") @PathVariable Long pocketId) throws Exception {
        PocketDTO.PocketWithJoinUserResponse response = pocketService.getJoinedUserOfPocket(pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketDetailInfoResponse.class)))})
    @Operation(summary = "포켓 상세정보 조회", description = "포켓 상세정보를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pocketId}")
    @Permission
    private ResponseEntity<PocketDTO.PocketDetailInfoResponse> getPocket(@Parameter(description = "포켓의 id") @PathVariable Long pocketId) throws Exception {
        PocketDTO.PocketDetailInfoResponse response = pocketService.getPocketDetail(pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoResponse.class)))})
    @Operation(summary = "포켓 탈퇴", description = "포켓에서 탈퇴 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pocketId}/leave")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> leavePocket(@AuthUser Users user, @Parameter(description = "포켓의 id") @PathVariable Long pocketId) throws Exception {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.leavePocket(user, pocketId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoResponse.class)))})
    @Operation(summary = "포켓에서 강퇴", description = "방장 권한일 경우, 포켓에서 강퇴 시킨다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{pocketId}/ban")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> banUserFromPocket(
            @AuthUser Users user,
            @Parameter(description = "포켓의 id") @PathVariable Long pocketId,
            @RequestBody PocketDTO.BanUserRequest banUserRequest) {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.banUserFromPocket(user, pocketId, banUserRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.InviteUserResponse.class)))})
    @Operation(summary = "포켓에 초대", description = "포켓에 초대를 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{pocketId}/invite")
    @Permission
    private ResponseEntity<PocketDTO.InviteUserResponse> invitePocket(
            @AuthUser Users user,
            @Parameter(description = "포켓의 id") @PathVariable Long pocketId,
            @RequestBody @Valid PocketDTO.InviteUserRequest request) {
        PocketDTO.InviteUserResponse response = pocketService.inviteUser(user, pocketId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.InviteUserFromLinkResponse.class)))})
    @Operation(summary = "링크를 사용한 포켓에 초대", description = "유저가 링크를 타고 들어올 경우 로그인 진행 후 포켓에 초대를 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/invite-key/{pocketKey}")
    @Permission
    private ResponseEntity<PocketDTO.InviteUserFromLinkResponse> invitePocketFromLink(@AuthUser Users user, @Parameter(description = "포켓의 key") @PathVariable("pocketKey") String pocketKey) {
        PocketDTO.InviteUserFromLinkResponse response = pocketService.inviteUserFromLink(user, pocketKey);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.acceptPocketInvitationResponse.class)))})
    @Operation(summary = "포켓 초대 수락", description = "포켓 초대를 수락 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{pocketId}/accept")
    @Permission
    private ResponseEntity<PocketDTO.acceptPocketInvitationResponse> acceptPocketInvitation(@AuthUser Users user, @Parameter(description = "포켓의 id") @PathVariable Long pocketId) {
        PocketDTO.acceptPocketInvitationResponse response = pocketService.acceptPocketInvitation(user, pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoResponse.class)))})
    @Operation(summary = "포켓 이름 수정", description = "포켓 이름을 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pocketId}")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> updatePocket(@AuthUser Users user,
                                                                            @Parameter(description = "포켓의 id") @PathVariable Long pocketId,
                                                                            @Valid @RequestPart(required = false) PocketDTO.UpdatePocketRequest request, @RequestPart(required = false) MultipartFile picture) {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.updatePocket(user, pocketId, request, picture);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketWithJoinUserResponse.class)))})
    @Operation(summary = "포켓 방장 권한 위임", description = "포켓 방장 권한을 위임 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pocketId}/master")
    @Permission
    private ResponseEntity<PocketDTO.PocketWithJoinUserResponse> updatePocketMaster(@AuthUser Users user, @Parameter(description = "포켓의 id") @PathVariable Long pocketId, @RequestBody PocketDTO.UpdatePocketMasterRequest request) {
        PocketDTO.PocketWithJoinUserResponse response = pocketService.updatePocketMaster(user, pocketId, request.getUserId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.NotAcceptPocketInviteUserResponse.class)))})
    @Operation(summary = "포켓별 초대를 수락하지 않은 유저 조회", description = "포켓별로 초대를 수락하지 않은 유저 리스트를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pocketId}/not-accept-invite")
    @Permission
    private ResponseEntity<PocketDTO.NotAcceptPocketInviteUserResponse> getAllNotAcceptInviteUser(@AuthUser Users user, @Parameter(description = "포켓의 id") @PathVariable Long pocketId) {
        PocketDTO.NotAcceptPocketInviteUserResponse response = pocketService.getAllNotAcceptPocketInviteUser(user, pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.cancelInvitePocketResponse.class)))})
    @Operation(summary = "초대 취소", description = "초대를 수락하지 않았을 경우에 초대를 취소 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pocketId}/cancel-invite-user/{cancelUserId}")
    @Permission
    private ResponseEntity<PocketDTO.cancelInvitePocketResponse> cancelPocketInvitation(
            @AuthUser Users user,
            @Parameter(description = "포켓의 id") @PathVariable Long pocketId,
            @Parameter(description = "취소하는 유저의 id") @PathVariable Long cancelUserId
    ) {
        PocketDTO.cancelInvitePocketResponse response = pocketService.cancelPocketInvitation(user, pocketId, cancelUserId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}