package com.project.domain.friend.repository;

import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.dto.QFriendDTO_FriendResponse;
import com.project.domain.friend.entity.Friend;
import com.project.domain.users.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.friend.entity.QFriend.friend;


@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory query;

    @Overridew
    public List<Friend> findAllByUserId(Long userId) {
        return query
                .selectFrom(friend)
                .where(friend.me.id.eq(userId))
                .stream().toList();
    }

    @Override
    public boolean existsByUserIds(Long myId, Long friendId) {

        Integer fetchFirst = query
                .selectOne()
                .from(friend)
                .where(friend.me.id.eq(myId))
                .where(friend.mate.id.eq(friendId))
                .fetchFirst();

        return fetchFirst != null;

    }

    @Override
    public List<FriendDTO.FriendResponse> findAllFriendsOfUser(Long userId) {

        return query
                .select(new QFriendDTO_FriendResponse(friend))
                .from(friend)
                .where(friend.me.id.eq(userId))
                .fetch();
    }

}
