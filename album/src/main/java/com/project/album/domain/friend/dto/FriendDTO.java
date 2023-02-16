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
        private Long id;
        private Long userId;
        private Long friendId;

        public FriendDetailResponse(Friend friend) {
            this.id = friend.getId();
            this.userId = friend.getMe().getId();
            this.friendId = friend.getFriend().getId();
        }
    }

    @Data
    public static class CreateFriendRequest{
        private Users user;
        private Users friend;

        public Friend toEntity() {
            return Friend.builder()
                    .me(user)
                    .friend(friend)
                    .build();
        }
    }
}
