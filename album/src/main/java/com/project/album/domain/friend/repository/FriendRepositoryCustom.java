package com.project.album.domain.friend.repository;

import com.project.album.domain.friend.entity.Friend;

import java.util.List;

public interface FriendRepositoryCustom {

    List<Friend> findByUserId(Long userId);
}
