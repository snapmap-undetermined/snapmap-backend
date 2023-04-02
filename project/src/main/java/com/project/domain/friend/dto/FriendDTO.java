package com.project.domain.friend.dto;

import com.project.domain.friend.entity.Friend;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class FriendDTO {

    @Data
    public static class FriendResponse {
        private Long id;
        private Long mateId;
        private String mateNickname;
        private String mateProfileImage;

        @QueryProjection
        public FriendResponse(Friend mate) {
            this.id = mate.getId();
            this.mateId = mate.getMate().getId();
            this.mateNickname = mate.getFriendName();
            this.mateProfileImage = mate.getMate().getProfileImage();
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
    }

    @Data
    public static class CreateFriendRequest {
        private Long friendUserId;
    }
}
