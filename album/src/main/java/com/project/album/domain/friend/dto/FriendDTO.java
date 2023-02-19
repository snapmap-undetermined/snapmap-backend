package com.project.album.domain.friend.dto;

import com.project.album.domain.friend.entity.Friend;
import lombok.Data;

public class FriendDTO {

    // GetFriend
    @Data
    public static class FriendSimpleInfoResponse {
        private Long friendId;
        private Long friendUserId;
        private String friendUserNickName;

        //        private String friendProfileImage;
        public FriendSimpleInfoResponse(Friend friend) {
            this.friendId = friend.getId();
            this.friendUserId = friend.getFriend().getId();
            this.friendUserNickName = friend.getFriendName();
        }
    }

    @Data
    public static class UpdateFriendNameRequest {
        private String friendName;
    }

    @Data
    public static class UpdateFriendNameResponse {
        private Long friendId;
        private Long friendUserId;
        private String friendName;

        public UpdateFriendNameResponse(Friend friend) {
            this.friendId = friend.getId();
            this.friendUserId = friend.getFriend().getId();
            this.friendName = friend.getFriendName();
        }
    }

    @Data
    public static class CreateFriendRequest {
        private Long friendUserId;
        private String friendName;

        public Friend toEntity() {
            return Friend.builder()
                    .friendName(friendName)
                    .build();
        }

    }
}
