package com.project.domain.friend.dto;

import com.project.domain.friend.entity.Friend;
import com.project.domain.users.entity.Users;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

public class FriendDTO {

    // GetFriend
    @Data
    public static class FriendResponse {
        private Long id;
        private Long userId;
        private String userNickName;
        private String userProfileImage;
        private Boolean activated;

        @QueryProjection
        public FriendResponse(Friend friend) {
            this.id = friend.getId();
            this.userId = friend.getMate().getId();
            this.userNickName = friend.getFriendName();
            this.userProfileImage = friend.getMate().getProfileImage();
            this.activated = friend.getActivated();
        }
    }

    @Data
    public static class FriendListResponse {
        private List<FriendResponse> friendListResponseList;

        public FriendListResponse(List<FriendResponse> friendResponseList) {
            this.friendListResponseList = friendResponseList;
        }
    }

    @Data
    public static class UpdateFriendNameRequest {

        @NotBlank(message = "친구 이름을 입력하세요.")
        private String friendName;

        public UpdateFriendNameRequest(String friendName) {
            this.friendName = friendName;
        }
    }

    @Data
    public static class CreateFriendRequest {
        private Long friendUserId;
        private String friendName;

        public Friend toEntity() {
            return Friend.builder()
                    .activated(true)
                    .build();
        }
        public CreateFriendRequest(Users mate) {
            this.friendName = mate.getNickname();
            this.friendUserId = mate.getId();
        }

    }
}
