package com.project.album.domain.friend.repository;

import com.project.album.domain.friend.entity.Friend;
import com.project.album.domain.friend.entity.QFriend;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Friend> findByUserId(Long userId) {
        QFriend f = new QFriend("f");

        return query
                .select(f)
                .from(f)
                .where(f.me.id.eq(userId))
                .stream().toList();
    }

}
