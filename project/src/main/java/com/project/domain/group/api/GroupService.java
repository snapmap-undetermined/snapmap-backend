package com.project.domain.group.api;

import com.project.domain.group.dto.GroupDTO;
import com.project.domain.users.entity.Users;
import org.springframework.web.multipart.MultipartFile;

public interface GroupService {

    GroupDTO.GroupSimpleInfoResponse createGroup(Users user, GroupDTO.CreateGroupRequest createGroupRequest);

    GroupDTO.GroupSimpleInfoListResponse getAllGroupByUser(Long userId);

    GroupDTO.GroupDetailInfoResponse getGroupDetail(Long groupId);

    GroupDTO.GroupWithJoinUserResponse getJoinedUserOfGroup(Long groupId) throws Exception;

    GroupDTO.GroupSimpleInfoResponse leaveGroup(Users user, Long groupId) throws Exception;

    GroupDTO.GroupSimpleInfoResponse banUserFromGroup(Users user, Long groupId, GroupDTO.BanUserRequest banUserRequest);

    GroupDTO.InviteUserResponse inviteUser(Users user, Long groupId, GroupDTO.InviteUserRequest request);

    GroupDTO.InviteUserFromLinkResponse inviteUserFromLink(Users user, String groupKey);

    GroupDTO.acceptGroupInvitationResponse acceptGroupInvitation(Users user, Long groupId);

    GroupDTO.cancelInviteGroupResponse cancelGroupInvitation(Users user, Long groupId, Long cancelUserId);

    GroupDTO.GroupSimpleInfoResponse updateGroup(Users user, Long groupId, GroupDTO.UpdateGroupRequest request, MultipartFile picture);

    GroupDTO.GroupWithJoinUserResponse updateGroupMaster(Users user, Long groupId, Long userId);

    GroupDTO.NotAcceptGroupInviteUserResponse getAllNotAcceptGroupInviteUser(Users user, Long groupId);

}
