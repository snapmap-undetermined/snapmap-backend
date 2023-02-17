package com.project.album.domain.friend.api;

import com.project.album.domain.friend.dto.FriendDTO;

import java.util.List;

public interface FriendService {

    List<FriendDTO.GetFriendListResponse> getFriendListByUser(Long userId) throws Exception;

    FriendDTO.FriendDetailResponse createFriend(Long userId, FriendDTO.CreateFriendRequest createFriendRequest) throws Exception;

    void deleteFriend(Long friendId) throws Exception;

    FriendDTO.UpdateFriendNameResponse updateFriendName(Long friendId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception;

}
