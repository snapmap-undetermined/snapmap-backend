package com.project.domain.group.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.handler.S3Uploader;
import com.project.domain.group.dto.GroupDTO;
import com.project.domain.group.entity.GroupData;
import com.project.domain.group.repository.GroupRepository;
import com.project.domain.usergroup.entity.UserGroup;
import com.project.domain.usergroup.repository.UserGroupRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public GroupDTO.GroupSimpleInfoResponse createGroup(Users user, GroupDTO.CreateGroupRequest request) {
        GroupData group = request.toEntity();
        group.setGroupKey(group.generateGroupKey());
        group.setMaster(user);

        UserGroup userGroup = UserGroup.builder().user(user).activated(true).group(group).build();
        userGroup.addUserGroupToUserAndGroup(user, group);

        // 그룹 생성 시, 친구를 같이 초대하는 경우 처리
        if (request.getInvitedUserList() != null && !request.getInvitedUserList().isEmpty()) {
            request.getInvitedUserList().forEach((userId) -> {
                Users u = userRepository.findById(userId).orElseThrow();
                UserGroup ug = UserGroup.builder().group(group).activated(false).user(u).build();
                ug.addUserGroupToUserAndGroup(u, group);
                userGroupRepository.save(ug);
            });
        }
        groupRepository.save(group);

        return new GroupDTO.GroupSimpleInfoResponse(group);
    }

    @Override
    public GroupDTO.GroupSimpleInfoListResponse getAllGroupByUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("User does not exist.");
        }

        List<GroupData> groupList = groupRepository.findAllGroupByUserId(userId);
        List<GroupDTO.GroupSimpleInfoResponse> response = groupList.stream().map(GroupDTO.GroupSimpleInfoResponse::new).collect(Collectors.toList());

        return new GroupDTO.GroupSimpleInfoListResponse(response);

    }

    @Override
    public GroupDTO.GroupDetailInfoResponse getGroupDetail(Long groupId) {
        GroupData group = getGroup(groupId);
        return new GroupDTO.GroupDetailInfoResponse(group);
    }

    @Override
    public GroupDTO.GroupWithJoinUserResponse getJoinedUserOfGroup(Long groupId) {
        GroupData group = getGroup(groupId);
        return new GroupDTO.GroupWithJoinUserResponse(group);
    }

    // 본인이 스스로 나감
    @Override
    @Transactional
    public GroupDTO.GroupSimpleInfoResponse leaveGroup(Users user, Long groupId) {
        GroupData group = getGroup(groupId);
        if (isMasterUser(group, user.getId()) && group.getUserGroupList().size() > 1) {
            throw new BusinessLogicException("Manager cannot leave group", ErrorCode.CIRCLE_MANAGER_ERROR);
        }

        // 유저가 혼자 남았을 경우에 그룹을 나가게 되면 해당 그룹이 삭제된다.
        if (group.getUserGroupList().size() == 1) {
            UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(user.getId(), group.getId()).orElseThrow();
            userGroup.removeUserGroupFromUserAndGroup(user, group);
            groupRepository.delete(group);
        } else {
            UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(user.getId(), group.getId()).orElseThrow();
            userGroup.removeUserGroupFromUserAndGroup(user, group);
        }

        return new GroupDTO.GroupSimpleInfoResponse(group);
    }

    // 방장이 유저를 추방함
    @Override
    @Transactional
    public GroupDTO.GroupSimpleInfoResponse banUserFromGroup(Users user, Long groupId, GroupDTO.BanUserRequest banUserRequest) {

        GroupData group = groupRepository.findById(groupId).orElseThrow();
        // 방장 권한일 경우
        if (isMasterUser(group, user.getId())) {
            UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(banUserRequest.getUserId(), groupId).orElseThrow();
            userGroup.removeUserGroupFromUserAndGroup(user, group);
        }
        return new GroupDTO.GroupSimpleInfoResponse(group);
    }

    // 그룹에 유저를 초대
    @Override
    @Transactional
    public GroupDTO.InviteUserResponse inviteUser(Users user, Long groupId, GroupDTO.InviteUserRequest request) {
        GroupData group = getGroup(groupId);
        List<Long> invitedUserList = request.getInvitedUserList();

        for (Long userId : invitedUserList) {
            Users u = getUser(userId);
            Optional<UserGroup> userGroup = userGroupRepository.findByUserIdAndGroupId(u.getId(), groupId);
            if (userGroup.isPresent()) {
                continue;
            }
            UserGroup ug = UserGroup.builder().user(u).group(group).activated(false).build(); // activated = false : 수락 이전 상태
            userGroupRepository.save(ug);
        }

        return new GroupDTO.InviteUserResponse(group, user);
    }

    // 앱 설치 후 유저가 링크를 타고 들어올 경우, 로그인을 완료했을 경우 그룹 초대를 자동으로 한다.
    @Override
    public GroupDTO.InviteUserFromLinkResponse inviteUserFromLink(Users user, String groupKey) {
        GroupData group = groupRepository.findGroupByKey(groupKey);
        UserGroup userGroup = UserGroup.builder().group(group).user(user).activated(false).build();
        userGroupRepository.save(userGroup);

        return new GroupDTO.InviteUserFromLinkResponse(userGroup);
    }

    // 유저가 초대 요청을 수락
    @Override
    @Transactional
    public GroupDTO.acceptGroupInvitationResponse acceptGroupInvitation(Users user, Long groupId) {
        GroupData group = getGroup(groupId);
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(user.getId(), groupId).orElseThrow();
        userGroup.setActivated(userGroup.getActivated());
        userGroup.addUserGroupToUserAndGroup(user, group);

        return new GroupDTO.acceptGroupInvitationResponse(user, userGroup);
    }

    @Override
    @Transactional
    public GroupDTO.cancelInviteGroupResponse cancelGroupInvitation(Users user, Long groupId, Long cancelUserId) {
        GroupData group = getGroup(groupId);
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(cancelUserId, groupId).orElseThrow(() -> {
            throw new EntityNotFoundException("User does not exists.");
        });
        // 요청을 보내는 유저가 해당 그룹에 속해있어야 초대 취소가 가능하다.
        if (group.getUserGroupList().stream()
                .filter(UserGroup::getActivated)
                .map(UserGroup::getUser).toList().contains(user)) {
            userGroup.removeUserGroupFromUserAndGroup(user, group);
        }

        return new GroupDTO.cancelInviteGroupResponse(user, group);
    }

    @Override
    @Transactional
    public GroupDTO.GroupSimpleInfoResponse updateGroup(Users user, Long groupId, GroupDTO.UpdateGroupRequest request, MultipartFile picture) {
        GroupData group = getGroup(groupId);
        if (isMasterUser(group, user.getId())) {
            group.setName(request.getGroupName());
            group.setDescription(request.getDescription());
            if (picture != null && !picture.isEmpty()) {
                String imageUrl = s3Uploader.uploadAndSaveImage(picture);
                group.setImageUrl(imageUrl);
            }
        } else {
            throw new BusinessLogicException("Only the manager can modify group settings.", ErrorCode.CIRCLE_MANAGER_ERROR);
        }
        return new GroupDTO.GroupSimpleInfoResponse(group);
    }

    // 유저가 방장일 경우, 방장 권한을 위임한다.
    @Override
    @Transactional
    public GroupDTO.GroupWithJoinUserResponse updateGroupMaster(Users user, Long groupId, Long userId) {
        GroupData group = getGroup(groupId);
        if (isMasterUser(group, user.getId())) {
            Users u = userRepository.findById(userId).orElseThrow();
            // 위임하려는 유저가 해당 그룹에 속해 있어야 한다.
            if (!isUserInGroup(u, groupId)) {
                throw new EntityNotFoundException("User manager delegated does not exists", ErrorCode.CIRCLE_MANAGER_ERROR);
            }
            group.setMaster(u);
        }
        return new GroupDTO.GroupWithJoinUserResponse(group);
    }

    @Override
    public GroupDTO.NotAcceptGroupInviteUserResponse getAllNotAcceptGroupInviteUser(Users user, Long groupId) {
        GroupData group = getGroup(groupId);
        return new GroupDTO.NotAcceptGroupInviteUserResponse(group);
    }

    private boolean isMasterUser(GroupData group, Long userId) {
        return group.getMaster().getId().equals(userId);
    }

    private GroupData getGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> {
            throw new EntityNotFoundException("Group does not exists.");
        });
    }

    private Users getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new EntityNotFoundException("User does not exists.");
        });
    }

    private boolean isUserInGroup(Users user, Long groupId) {
        GroupData group = getGroup(groupId);
        return group.getUserGroupList().stream()
                .map(UserGroup::getUser).toList().contains(user);
    }
}
