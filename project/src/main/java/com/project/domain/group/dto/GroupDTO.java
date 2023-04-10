package com.project.domain.group.dto;


import com.project.domain.group.entity.GroupData;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.usergroup.entity.UserGroup;
import com.project.domain.users.dto.UserDTO.UserSimpleInfoResponse;
import com.project.domain.users.entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GroupDTO {

    @Data
    public static class GroupSimpleInfoResponse {
        private Long groupId;
        private String groupName;
        private String groupImageUrl;
        private Integer joinedUserCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public GroupSimpleInfoResponse(GroupData group) {
            this.groupId = group.getId();
            this.groupName = group.getName();
            this.groupImageUrl = group.getImageUrl();
            this.joinedUserCount = group.getUserGroupList().size();
            this.createdAt = group.getCreatedAt();
            this.updatedAt = group.getModifiedAt();
        }
    }

    @Data
    public static class GroupSimpleInfoListResponse {
        private List<GroupSimpleInfoResponse> groupSimpleInfoResponseList;

        public GroupSimpleInfoListResponse(List<GroupSimpleInfoResponse> groupSimpleInfoResponseList) {
            this.groupSimpleInfoResponseList = groupSimpleInfoResponseList;
        }
    }

    @Data
    public static class GroupDetailInfoResponse {
        private Long groupId;
        private String groupName;
        private String imageUrl;
        private String description;
        private Integer userCount;
        private Integer pinCount;
        private Integer pictureCount;
        private List<UserSimpleInfoResponse> joinedUserList;
        private PinDTO.PinDetailListResponse pinList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public GroupDetailInfoResponse(GroupData group) {
            this.groupId = group.getId();
            this.groupName = group.getName();
            this.imageUrl = group.getImageUrl();
            this.description = group.getDescription();
            this.userCount = group.getUserGroupList().size();
            this.pinCount = group.getPinList().size();
            this.pictureCount = group.getPinList().stream().mapToInt(pl -> pl.getPictures().size()).sum();
            this.joinedUserList = group.getUserGroupList().stream().map(uc -> new UserSimpleInfoResponse(uc.getUser())).collect(Collectors.toList());
            this.pinList = new PinDTO.PinDetailListResponse(group.getPinList().stream()
                    .map(PinDTO.PinDetailResponse::new)
                    .collect(Collectors.toList()));
            this.createdAt = group.getCreatedAt();
            this.updatedAt = group.getModifiedAt();
        }
    }

    @Data
    public static class CreateGroupRequest {

        @NotBlank(message = "그룹 이름을 입력해주세요.")
        private String groupName;
        private String description;
        private List<Long> invitedUserList;
        private String imageUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png?w=1480&t=st=1679211933~exp=1679212533~hmac=b61bdb1145eb754a852d3c13ed5006de6ee4c0b4b0dd6c5f8575e7828f7ff977";

        public GroupData toEntity() {
            return GroupData.builder()
                    .name(groupName)
                    .description(description)
                    .imageUrl(imageUrl)
                    .build();
        }
    }

    @Data
    public static class UpdateGroupRequest {
        @NotBlank(message = "그룹 이름을 입력해주세요.")
        private String groupName;
        private String description;
    }

    @Data
    public static class GroupWithJoinUserResponse {

        private Long groupId;
        private String groupName;
        private String description;
        private String groupImageUrl;
        private UserSimpleInfoResponse master;
        private List<UserSimpleInfoResponse> joinedUserList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public GroupWithJoinUserResponse(GroupData group) {
            this.groupId = group.getId();
            this.groupName = group.getName();
            this.description = group.getDescription();
            this.groupImageUrl = group.getImageUrl();
            this.master = new UserSimpleInfoResponse(group.getMaster());
            this.joinedUserList = group.getUserGroupList().stream()
                    .filter(UserGroup::getActivated)
                    .map(UserGroup::getUser)
                    .filter(user -> !user.equals(group.getMaster()))
                    .map(UserSimpleInfoResponse::new)
                    .collect(Collectors.toList());
            this.createdAt = group.getCreatedAt();
            this.updatedAt = group.getModifiedAt();
        }
    }

    @Data
    public static class InviteUserRequest {
        @Size(min = 1, message = "한 명 이상의 유저를 선택해야 합니다.")
        private List<Long> invitedUserList;
    }

    @Data
    public static class InviteUserResponse {

        private Long groupId;
        private Long userId;
        private String userNickname;
        private List<UserSimpleInfoResponse> userList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


        public InviteUserResponse(GroupData group, Users user) {
            this.groupId = group.getId();
            this.userId = user.getId();
            this.userNickname = user.getNickname();
            this.userList = group.getUserGroupList().stream()
                    .filter(userGroup -> !userGroup.getActivated())
                    .map((uc) -> new UserSimpleInfoResponse(uc.getUser()))
                    .collect(Collectors.toList());
            this.createdAt = group.getCreatedAt();
            this.updatedAt = group.getModifiedAt();
        }
    }

    @Data
    public static class InviteUserFromLinkResponse {
        private Long groupId;
        private UserSimpleInfoResponse user;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public InviteUserFromLinkResponse(UserGroup userGroup) {
            this.groupId = userGroup.getGroup().getId();
            this.user = new UserSimpleInfoResponse(userGroup.getUser());
            this.createdAt = userGroup.getCreatedAt();
            this.updatedAt = userGroup.getModifiedAt();
        }
    }

    @Data
    public static class BanUserRequest {

        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private Long userId;
    }

    @Data
    public static class UpdateGroupMasterRequest {

        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private Long userId;
    }

    @Data
    public static class acceptGroupInvitationResponse {
        private Long userId;
        private Long groupId;
        private Boolean activated;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public acceptGroupInvitationResponse(Users user, UserGroup userGroup) {
            this.userId = userGroup.getUser().getId();
            this.groupId = userGroup.getGroup().getId();
            this.activated = userGroup.getActivated();
            this.createdAt = userGroup.getCreatedAt();
            this.updatedAt = userGroup.getModifiedAt();
        }
    }

    @Data
    public static class NotAcceptGroupInviteUserResponse {

        private List<UserSimpleInfoResponse> userList;

        public NotAcceptGroupInviteUserResponse(GroupData group) {

            this.userList = group.getUserGroupList().stream()
                    .filter(userGroup -> !userGroup.getActivated())
                    .map((ug) -> new UserSimpleInfoResponse(ug.getUser()))
                    .collect(Collectors.toList());
        }
    }

    @Data
    public static class cancelInviteGroupResponse {
        private Long userId;
        private Long groupId;
        private LocalDateTime updatedAt;

        public cancelInviteGroupResponse(Users user, GroupData group) {
            this.userId = user.getId();
            this.groupId = group.getId();
            this.updatedAt = LocalDateTime.now();

        }
    }

}
