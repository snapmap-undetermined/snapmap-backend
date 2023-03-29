package com.project.domain.circle.repository;

import com.project.domain.circle.entity.Circle;
import com.project.domain.users.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.circle.entity.QCircle.circle;
import static com.project.domain.usercircle.entity.QUserCircle.userCircle;
import static com.project.domain.users.entity.QUsers.users;


@RequiredArgsConstructor
public class CircleRepositoryCustomImpl implements CircleRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Circle> findAllCircleByUserId(Long userId) {
        return query
                .selectFrom(circle)
                .innerJoin(circle.userCircleList, userCircle)
                .fetchJoin()
                .where(userCircle.user.id.eq(userId))
                .where(userCircle.activated.eq(true))
                .distinct()
                .fetch();

    }

    @Override
    public List<Users> findAllUserByCircleId(Long circleId) {

        return query
                .selectFrom(users)
                .innerJoin(users.userCircleList, userCircle)
                .fetchJoin()
                .where(userCircle.circle.id.eq(circleId))
                .where(userCircle.activated.eq(true))
                .distinct()
                .fetch();
    }

    @Override
    public Circle findCircleByKey(String key) {
        return query
                .selectFrom(circle)
                .where(circle.circleKey.eq(key))
                .fetchOne();
    }
}
