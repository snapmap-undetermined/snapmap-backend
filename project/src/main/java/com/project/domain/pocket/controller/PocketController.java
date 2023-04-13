package com.project.domain.pocket.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.pocket.api.PocketService;
import com.project.domain.pocket.dto.PocketDTO;
import com.project.domain.users.entity.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/pocket")
public class PocketController {

    private final PocketService pocketService;

    //그룹을 생성한다.
    @PostMapping("")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> createPocket(@AuthUser Users user, @Valid @RequestBody PocketDTO.CreatePocketRequest createPocketRequest) throws Exception {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.createPocket(user, createPocketRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저별로 그룹을 조회한다.
    @GetMapping("")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoListResponse> getAllPocketByUser(@AuthUser Users user) {
        PocketDTO.PocketSimpleInfoListResponse response = pocketService.getAllPocketByUser(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹을 선택해서 그룹원들을 조회한다.
    @GetMapping("/{pocketId}/users")
    @Permission
    private ResponseEntity<PocketDTO.PocketWithJoinUserResponse> getUserListByPocket(@PathVariable Long pocketId) throws Exception {
        PocketDTO.PocketWithJoinUserResponse response = pocketService.getJoinedUserOfPocket(pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 그룹 상세 조회
    @GetMapping("/{pocketId}")
    @Permission
    private ResponseEntity<PocketDTO.PocketDetailInfoResponse> getPocket(@PathVariable Long pocketId) throws Exception {
        PocketDTO.PocketDetailInfoResponse response = pocketService.getPocketDetail(pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹에서 나온다.
    @PatchMapping("/{pocketId}/leave")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> leavePocket(@AuthUser Users user, @PathVariable Long pocketId) throws Exception {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.leavePocket(user, pocketId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //방장이 유저를 강퇴한다.
    @PostMapping("/{pocketId}/ban")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> banUserFromPocket(@AuthUser Users user, @PathVariable Long pocketId, @RequestBody PocketDTO.BanUserRequest banUserRequest) {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.banUserFromPocket(user, pocketId, banUserRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저를 그룹에 초대한다.
    @PostMapping("/{pocketId}/invite")
    @Permission
    private ResponseEntity<PocketDTO.InviteUserResponse> invitePocket(@AuthUser Users user, @PathVariable Long pocketId, @RequestBody @Valid PocketDTO.InviteUserRequest request) {
        PocketDTO.InviteUserResponse response = pocketService.inviteUser(user, pocketId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 링크를 타고 들어올 경우, 링크값으로 써클을 찾고, 로그인을 완료했을 경우 그룹 초대를 하도록 한다.
    @PostMapping("/invite-key/{pocketKey}")
    @Permission
    private ResponseEntity<PocketDTO.InviteUserFromLinkResponse> invitePocketFromLink(@AuthUser Users user, @PathVariable("pocketKey") String pocketKey) {
        PocketDTO.InviteUserFromLinkResponse response = pocketService.inviteUserFromLink(user, pocketKey);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹 초대를 수락한다.
    @PostMapping("/{pocketId}/accept")
    @Permission
    private ResponseEntity<PocketDTO.acceptPocketInvitationResponse> acceptPocketInvitation(@AuthUser Users user, @PathVariable Long pocketId) {
        PocketDTO.acceptPocketInvitationResponse response = pocketService.acceptPocketInvitation(user, pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹이름을 수정한다.
    @PatchMapping("/{pocketId}")
    @Permission
    private ResponseEntity<PocketDTO.PocketSimpleInfoResponse> updatePocket(@AuthUser Users user, @PathVariable Long pocketId,
                                                                            @Valid @RequestPart(required = false) PocketDTO.UpdatePocketRequest request, @RequestPart(required = false) MultipartFile picture) {
        PocketDTO.PocketSimpleInfoResponse response = pocketService.updatePocket(user, pocketId, request, picture);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 방장인 유저가 방장 권한을 위임한다.
    @PatchMapping("/{pocketId}/master")
    @Permission
    private ResponseEntity<PocketDTO.PocketWithJoinUserResponse> updatePocketMaster(@AuthUser Users user, @PathVariable Long pocketId, @RequestBody PocketDTO.UpdatePocketMasterRequest request) {
        PocketDTO.PocketWithJoinUserResponse response = pocketService.updatePocketMaster(user, pocketId, request.getUserId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 그룹별로 아직 초대를 수락하지 않은 유저들 조회
    @GetMapping("/{pocketId}/not-accept-invite")
    @Permission
    private ResponseEntity<PocketDTO.NotAcceptPocketInviteUserResponse> getAllNotAcceptInviteUser(@AuthUser Users user, @PathVariable Long pocketId) {
        PocketDTO.NotAcceptPocketInviteUserResponse response = pocketService.getAllNotAcceptPocketInviteUser(user, pocketId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 아직 수락하지 않았을 경우 초대를 취소한다,
    @PatchMapping("/{pocketId}/cancel-invite-user/{cancelUserId}")
    @Permission
    private ResponseEntity<PocketDTO.cancelInvitePocketResponse> cancelPocketInvitation(
            @AuthUser Users user,
            @PathVariable Long pocketId,
            @PathVariable Long cancelUserId
    ) {
        PocketDTO.cancelInvitePocketResponse response = pocketService.cancelPocketInvitation(user, pocketId, cancelUserId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}