package com.project.domain.friend.api;

import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.users.entity.Users;

public interface FriendService {

    FriendDTO.FriendListResponse getAllFriends(Long userId) throws Exception;

    FriendDTO.FriendResponse createFriend(Users user, FriendDTO.CreateFriendRequest createFriendRequest) throws Exception;

    FriendDTO.FriendResponse deleteFriend(Long friendId) throws Exception;

    FriendDTO.FriendResponse updateFriendName(Long friendId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception;

}
