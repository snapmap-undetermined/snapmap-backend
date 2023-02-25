package com.project.domain.friend.api;

import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface FriendService {

    List<FriendDTO.FriendSimpleInfoResponse> getAllFriends(Long userId) throws Exception;

    FriendDTO.FriendSimpleInfoResponse createFriend(Users user, FriendDTO.CreateFriendRequest createFriendRequest) throws Exception;

    void deleteFriend(Long friendId) throws Exception;

    FriendDTO.FriendSimpleInfoResponse updateFriendName(Long friendId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception;

}
