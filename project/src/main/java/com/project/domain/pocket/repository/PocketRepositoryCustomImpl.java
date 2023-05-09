package com.project.domain.pocket.repository;

import com.project.domain.pocket.entity.Pocket;
import com.project.domain.users.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.pocket.entity.QPocket.pocket;
import static com.project.domain.userpocket.entity.QUserPocket.userPocket;
import static com.project.domain.users.entity.QUsers.users;


@RequiredArgsConstructor
public class PocketRepositoryCustomImpl implements PocketRepositoryCustom {

    private final JPAQueryFactory query;


    @Override
    public List<Pocket> findAllPocketByUserId(Long userId) {
        return query
                .selectFrom(pocket)
                .innerJoin(pocket.userPocketList, userPocket)
                .fetchJoin()
                .where(userPocket.user.id.eq(userId))
                .where(userPocket.activated.eq(true))
                .distinct()
                .fetch();

    }

    @Override
    public Pocket findPocketByKey(String key) {
        return query
                .selectFrom(pocket)
                .where(pocket.pocketKey.eq(key))
                .fetchOne();
    }
}
