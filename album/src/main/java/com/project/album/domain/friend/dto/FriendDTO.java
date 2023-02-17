package com.project.album.domain.friend.dto;

import com.project.album.domain.friend.entity.Friend;
import com.project.album.domain.users.entity.Users;
import lombok.Data;

import java.util.List;

public class FriendDTO {

    // GetFriendList
    @Data
    public static class GetFriendListResponse {
        private Long userId;
        private List<Friend> myFriendList;

        public GetFriendListResponse(Friend friend) {
            this.userId = friend.getMe().getId();
            this.myFriendList = friend.getMe().getFriendList();
        }
    }

    // FriendDetailResponse 공통 responseDTO
    @Data
    public static class FriendDetailResponse {
        private Long friendId;
        private Long userId;
        private Long friendUserId;

        public FriendDetailResponse(Friend friend) {
            this.friendId = friend.getId();
            this.userId = friend.getMe().getId();
            this.friendUserId = friend.getFriend().getId();
        }
    }

    @Data
    public static class UpdateFriendNameRequest{
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
    public static class CreateFriendRequest{
        private Users user;
        private Users friend;
        private String friendName;

        public Friend toEntity() {
            return Friend.builder()
                    .me(user)
                    .friend(friend)
                    .friendName(friend.getNickname())
                    .build();
        }
    }
}
