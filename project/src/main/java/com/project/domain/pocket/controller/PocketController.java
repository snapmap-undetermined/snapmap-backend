package com.project.domain.pocket.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
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
    @Operation(summary = "신규 포켓 생성", description = "새로운 포켓을 생성한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> createPocket(@AuthUser Users user, @Valid @RequestBody PocketDTO.CreatePocketRequest createPocketRequest) throws Exception {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.createPocket(user, createPocketRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoListResponse.class)))})
    @Operation(summary = "자신이 속한 포켓 리스트 조회", description = "자신이 속해 있는 포켓 리스트의 기본 정보를 조회한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoListResponse> getAllPocketByUser(@AuthUser Users user) {
        PocketDTO.PocketSimpleInfoListResponse response = pocketService.getAllPocketByUser(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketWithJoinUserResponse.class)))})
    @Operation(summary = "포켓에 속한 유저 리스트 조회", description = "특정 포켓에 속한 유저 리스트를 조회한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pocketId}/users")
    @Permission
    private ResponseEntity<PocketDTO.PocketWithJoinUserResponse> getUserListByPocket(@Parameter(description = "포켓의 ID") @PathVariable Long pocketId) throws Exception {
        PocketDTO.PocketWithJoinUserResponse response = pocketService.getJoinedUserOfPocket(pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketDetailInfoResponse.class)))})
    @Operation(summary = "포켓 상세정보 조회", description = "특정 포켓의 상세 정보를 조회한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pocketId}")
    @Permission
    private ResponseEntity<PocketDTO.PocketDetailInfoResponse> getPocket(@Parameter(description = "포켓의 ID") @PathVariable Long pocketId) {
        PocketDTO.PocketDetailInfoResponse response = pocketService.getPocketDetail(pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoResponse.class)))})
    @Operation(summary = "포켓 나가기", description = "해당 포켓에서 나간다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pocketId}/leave")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> leavePocket(@AuthUser Users user, @Parameter(description = "포켓의 ID") @PathVariable Long pocketId) throws Exception {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.leavePocket(user, pocketId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoResponse.class)))})
    @Operation(summary = "포켓 내 유저 강제 퇴장", description = "자신이 방장 권한일 경우, 특정 유저를 포켓에서 강제 퇴장시킨다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{pocketId}/ban")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> banUserFromPocket(
            @AuthUser Users user,
            @Parameter(description = "포켓의 ID") @PathVariable Long pocketId,
            @RequestBody PocketDTO.BanUserRequest banUserRequest) {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.banUserFromPocket(user, pocketId, banUserRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.InviteUserResponse.class)))})
    @Operation(summary = "유저 ID를 통해 직접 포켓 초대", description = "유저 ID를 기반으로 포켓에 초대한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{pocketId}/invite")
    @Permission
    private ResponseEntity<PocketDTO.InviteUserResponse> invitePocket(
            @AuthUser Users user,
            @Parameter(description = "포켓의 ID") @PathVariable Long pocketId,
            @RequestBody @Valid PocketDTO.InviteUserRequest request) {
        PocketDTO.InviteUserResponse response = pocketService.inviteUser(user, pocketId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.InviteUserFromLinkResponse.class)))})
    @Operation(summary = "링크를 통해 포켓 초대", description = "PocketKey 기반 링크를 통해 유저를 포켓에 초대한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/invite-key/{pocketKey}")
    @Permission
    private ResponseEntity<PocketDTO.InviteUserFromLinkResponse> invitePocketFromLink(@AuthUser Users user, @Parameter(description = "포켓의 Key") @PathVariable("pocketKey") String pocketKey) {
        PocketDTO.InviteUserFromLinkResponse response = pocketService.inviteUserFromLink(user, pocketKey);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.acceptPocketInvitationResponse.class)))})
    @Operation(summary = "포켓 초대 수락", description = "요청받은 포켓 초대를 수락한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{pocketId}/accept")
    @Permission
    private ResponseEntity<PocketDTO.acceptPocketInvitationResponse> acceptPocketInvitation(@AuthUser Users user, @Parameter(description = "포켓의 ID") @PathVariable Long pocketId) {
        PocketDTO.acceptPocketInvitationResponse response = pocketService.acceptPocketInvitation(user, pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketSimpleInfoResponse.class)))})
    @Operation(summary = "포켓 정보 수정", description = "포켓의 이름과 설명을 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pocketId}")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> updatePocket(@AuthUser Users user,
                                                                            @Parameter(description = "포켓의 ID") @PathVariable Long pocketId,
                                                                            @Valid @RequestPart(required = false) PocketDTO.UpdatePocketRequest request, @RequestPart(required = false) MultipartFile picture) {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.updatePocket(user, pocketId, request, picture);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.PocketWithJoinUserResponse.class)))})
    @Operation(summary = "포켓 내 방장 권한 위임", description = "자신의 방장 권한을 포켓 내 다른 유저에게 위임한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pocketId}/master")
    @Permission
    private ResponseEntity<PocketDTO.PocketWithJoinUserResponse> updatePocketMaster(@AuthUser Users user, @Parameter(description = "포켓의 ID") @PathVariable Long pocketId, @RequestBody PocketDTO.UpdatePocketMasterRequest request) {
        PocketDTO.PocketWithJoinUserResponse response = pocketService.updatePocketMaster(user, pocketId, request.getUserId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.NotAcceptPocketInviteUserResponse.class)))})
    @Operation(summary = "초대를 수락하지 않은 유저 조회", description = "포켓 별 자신의 초대를 수락하지 않은 유저 리스트를 조회한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pocketId}/not-accept-invite-users")
    @Permission
    private ResponseEntity<PocketDTO.NotAcceptPocketInviteUserResponse> getAllNotAcceptInviteUser(@AuthUser Users user, @Parameter(description = "포켓의 ID") @PathVariable Long pocketId) {
        PocketDTO.NotAcceptPocketInviteUserResponse response = pocketService.getAllNotAcceptPocketInviteUser(user, pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PocketDTO.cancelInvitePocketResponse.class)))})
    @Operation(summary = "초대 취소", description = "아직 포켓 초대를 수락하지 않은 경우, 초대를 취소 할 수 있다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pocketId}/cancel-invite-users/{cancelUserId}")
    @Permission
    private ResponseEntity<PocketDTO.cancelInvitePocketResponse> cancelPocketInvitation(
            @AuthUser Users user,
            @Parameter(description = "포켓의 ID") @PathVariable Long pocketId,
            @Parameter(description = "초대를 취소할 유저의 ID") @PathVariable Long cancelUserId
    ) {
        PocketDTO.cancelInvitePocketResponse response = pocketService.cancelPocketInvitation(user, pocketId, cancelUserId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}