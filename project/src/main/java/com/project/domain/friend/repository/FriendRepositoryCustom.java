package com.project.domain.friend.repository;

import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.entity.Friend;

import java.util.List;
import java.util.Optional;

public interface FriendRepositoryCustom {

    boolean existsByUserIds(Long myId, Long friendId);

    List<FriendDTO.FriendResponse> findAllFriendsOfUser(Long userId);

}
