package com.project.album.domain.friend.repository;

import com.project.album.domain.friend.entity.Friend;
import com.project.album.domain.friend.entity.QFriend;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Friend> findByUserId(Long userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QFriend f = new QFriend("f");

        return queryFactory
                .select(f)
                .from(f)
                .where(f.me.id.eq(userId))
                .stream().toList();
    }

}
