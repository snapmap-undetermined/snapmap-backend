package com.project.domain.friend.repository;

import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.entity.QFriend;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.friend.entity.QFriend.friend;

@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Friend> findAllByUserId(Long userId) {

        return query
                .selectFrom(friend)
                .where(friend.me.id.eq(userId))
                .stream().toList();
    }
}
