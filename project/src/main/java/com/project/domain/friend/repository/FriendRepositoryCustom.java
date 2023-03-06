package com.project.domain.friend.repository;

import com.project.domain.friend.entity.Friend;

import java.util.List;

public interface FriendRepositoryCustom {

    List<Friend> findAllByUserId(Long userId);
}
