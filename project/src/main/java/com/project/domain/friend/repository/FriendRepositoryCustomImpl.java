package com.project.domain.friend.repository;

import com.project.domain.friend.entity.Friend;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.friend.entity.QFriend.friend1;


@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Friend> findAllByUserId(Long userId) {

        return query
                .selectFrom(friend1)
                .where(friend1.me.id.eq(userId))
                .stream().toList();
    }

    @Override
    public boolean existByMeUserIdAndFriendUserId(Long meUserId, Long friendUserId) {

        Integer fetchFirst = query
                .selectOne()
                .from(friend1)
                .where(friend1.me.id.eq(meUserId))
                .where(friend1.friend.id.eq(friendUserId))
                .fetchFirst();

        return fetchFirst != null;

    }


}
