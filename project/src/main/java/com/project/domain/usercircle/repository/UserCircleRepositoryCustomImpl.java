package com.project.domain.usercircle.repository;

import com.project.domain.usercircle.entity.QUserCircle;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.entity.QUsers;
import com.project.domain.users.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

import static com.project.domain.usercircle.entity.QUserCircle.userCircle;
import static com.project.domain.users.entity.QUsers.users;

@RequiredArgsConstructor
public class UserCircleRepositoryCustomImpl implements UserCircleRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<UserCircle> findByUserId(Long userId) {

        return query
                .selectFrom(userCircle)
                .where(userCircle.user.id.eq(userId))
                .distinct()
                .fetch();
    }

    @Override
    public List<Users> findAllUserByCircleId(Long circleId) {

        return query
                .select(userCircle.user)
                .from(userCircle)
                .where(userCircle.circle.id.eq(circleId))
                .fetch();


    }
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
