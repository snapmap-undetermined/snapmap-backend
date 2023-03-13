package com.project.domain.circle.repository;

import com.project.domain.circle.entity.Circle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.circle.entity.QCircle.circle;
import static com.project.domain.usercircle.entity.QUserCircle.userCircle;


@RequiredArgsConstructor
public class CircleRepositoryCustomImpl implements CircleRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Circle> findAllCircleByUserId(Long userId) {
        return query
                .selectFrom(circle)
                .innerJoin(circle.userList, userCircle)
                .fetchJoin()
                .where(userCircle.user.id.eq(userId))
                .distinct()
                .fetch();

    }
}
