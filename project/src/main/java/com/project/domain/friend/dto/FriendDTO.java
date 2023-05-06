package com.project.domain.friend.dto;

import com.project.domain.friend.entity.Friend;
import com.project.domain.users.entity.Users;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class FriendDTO {

    @Data
    public static class FriendResponse {
        private Long id;
        private Long meId;
        private Long mateId;
        private String mateNickname;
        private String mateProfileImage;

        @QueryProjection
        public FriendResponse(Friend friend) {
            this.id = friend.getId();
            this.meId = friend.getMe().getId();
            this.mateId = friend.getMate().getId();
            this.mateNickname = friend.getFriendName();
            this.mateProfileImage = friend.getMate().getProfileImage();
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateFriendNameRequest {

        @NotBlank(message = "친구 이름을 입력해 주세요.")
        private String friendName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateFriendRequest {
        @NotBlank(message = "친구 아이디를 입력해 주세요.")
        private Long friendUserId;
    }
}
