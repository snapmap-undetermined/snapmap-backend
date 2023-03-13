package com.project.domain.usercircle.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import static com.project.domain.usercircle.entity.QUserCircle.userCircle;

@RequiredArgsConstructor
public class UserCircleRepositoryCustomImpl implements UserCircleRepositoryCustom {

    private final JPAQueryFactory query;
    @Override
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    public Long deleteByUserIdAndCircleId(Long userId, Long circleId) {

        return query
                .delete(userCircle)
                .where(userCircle.user.id.eq(userId))
                .where(userCircle.circle.id.eq(circleId))
                .execute();
    }

}
