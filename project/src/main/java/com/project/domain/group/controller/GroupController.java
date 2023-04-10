package com.project.domain.group.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.group.api.GroupService;
import com.project.domain.group.dto.GroupDTO;
import com.project.domain.users.entity.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/group")
public class GroupController {

    private final GroupService groupService;

    //그룹을 생성한다.
    @PostMapping("")
    @Permission
    private ResponseEntity<GroupDTO.GroupSimpleInfoResponse> createGroup(@AuthUser Users user, @Valid @RequestBody GroupDTO.CreateGroupRequest createGroupRequest) throws Exception {
        GroupDTO.GroupSimpleInfoResponse response = groupService.createGroup(user, createGroupRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저별로 그룹을 조회한다.
    @GetMapping("")
    @Permission
    private ResponseEntity<GroupDTO.GroupSimpleInfoListResponse> getAllGroupByUser(@AuthUser Users user) {
        GroupDTO.GroupSimpleInfoListResponse response = groupService.getAllGroupByUser(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹을 선택해서 그룹원들을 조회한다.
    @GetMapping("/{groupId}/users")
    @Permission
    private ResponseEntity<GroupDTO.GroupWithJoinUserResponse> getUserListByGroup(@PathVariable Long groupId) throws Exception {
        GroupDTO.GroupWithJoinUserResponse response = groupService.getJoinedUserOfGroup(groupId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 그룹 상세 조회
    @GetMapping("/{groupId}")
    @Permission
    private ResponseEntity<GroupDTO.GroupDetailInfoResponse> getGroup(@PathVariable Long groupId) throws Exception {
        GroupDTO.GroupDetailInfoResponse response = groupService.getGroupDetail(groupId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //유저가 그룹에서 나온다.
    @PatchMapping("/{groupId}/leave")
    @Permission
    private ResponseEntity<GroupDTO.GroupSimpleInfoResponse> leaveGroup(@AuthUser Users user, @PathVariable Long groupId) throws Exception {
        GroupDTO.GroupSimpleInfoResponse response = groupService.leaveGroup(user, groupId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //방장이 유저를 강퇴한다.
    @PostMapping("/{groupId}/ban")
    @Permission
    private ResponseEntity<GroupDTO.GroupSimpleInfoResponse> banUserFromGroup(@AuthUser Users user, @PathVariable Long groupId, @RequestBody GroupDTO.BanUserRequest banUserRequest) {
        GroupDTO.GroupSimpleInfoResponse response = groupService.banUserFromGroup(user, groupId, banUserRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저를 그룹에 초대한다.
    @PostMapping("/{groupId}/invite")
    @Permission
    private ResponseEntity<GroupDTO.InviteUserResponse> inviteGroup(@AuthUser Users user, @PathVariable Long groupId, @RequestBody @Valid GroupDTO.InviteUserRequest request) {
        GroupDTO.InviteUserResponse response = groupService.inviteUser(user, groupId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 링크를 타고 들어올 경우, 링크값으로 써클을 찾고, 로그인을 완료했을 경우 그룹 초대를 하도록 한다.
    @PostMapping("/invite-key/{groupKey}")
    @Permission
    private ResponseEntity<GroupDTO.InviteUserFromLinkResponse> inviteGroupFromLink(@AuthUser Users user, @PathVariable("groupKey") String groupKey) {
        GroupDTO.InviteUserFromLinkResponse response = groupService.inviteUserFromLink(user, groupKey);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹 초대를 수락한다.
    @PostMapping("/{groupId}/accept")
    @Permission
    private ResponseEntity<GroupDTO.acceptGroupInvitationResponse> acceptGroupInvitation(@AuthUser Users user, @PathVariable Long groupId) {
        GroupDTO.acceptGroupInvitationResponse response = groupService.acceptGroupInvitation(user, groupId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 유저가 그룹이름을 수정한다.
    @PatchMapping("/{groupId}")
    @Permission
    private ResponseEntity<GroupDTO.GroupSimpleInfoResponse> updateGroup(@AuthUser Users user, @PathVariable Long groupId,
                                                                          @Valid @RequestPart(required = false) GroupDTO.UpdateGroupRequest request, @RequestPart(required = false) MultipartFile picture) {
        GroupDTO.GroupSimpleInfoResponse response = groupService.updateGroup(user, groupId, request, picture);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 방장인 유저가 방장 권한을 위임한다.
    @PatchMapping("/{groupId}/master")
    @Permission
    private ResponseEntity<GroupDTO.GroupWithJoinUserResponse> updateGroupMaster(@AuthUser Users user, @PathVariable Long groupId, @RequestBody GroupDTO.UpdateGroupMasterRequest request) {
        GroupDTO.GroupWithJoinUserResponse response = groupService.updateGroupMaster(user, groupId, request.getUserId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 그룹별로 아직 초대를 수락하지 않은 유저들 조회
    @GetMapping("/{groupId}/not-accept-invite")
    @Permission
    private ResponseEntity<GroupDTO.NotAcceptGroupInviteUserResponse> getAllNotAcceptInviteUser(@AuthUser Users user, @PathVariable Long groupId) {
        GroupDTO.NotAcceptGroupInviteUserResponse response = groupService.getAllNotAcceptGroupInviteUser(user, groupId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 아직 수락하지 않았을 경우 초대를 취소한다,
    @PatchMapping("/{groupId}/cancel-invite-user/{cancelUserId}")
    @Permission
    private ResponseEntity<GroupDTO.cancelInviteGroupResponse> cancelInviteGroupResponse(
            @AuthUser Users user,
            @PathVariable Long groupId,
            @PathVariable Long cancelUserId
    ) {
        GroupDTO.cancelInviteGroupResponse response = groupService.cancelGroupInvitation(user, groupId, cancelUserId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}