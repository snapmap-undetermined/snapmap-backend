package com.project.domain.users.repository;

import com.project.domain.users.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.usercircle.entity.QUserCircle.userCircle;
import static com.project.domain.users.entity.QUsers.users;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Users> findAllUserByCircleId(Long circleId) {

        return query
                .selectFrom(users)
                .innerJoin(users.circleList, userCircle)
                .fetchJoin()
                .where(userCircle.circle.id.eq(circleId))
                .distinct()
                .fetch();
    }
}
