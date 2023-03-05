package com.project.domain.usercircle.repository;

import com.project.domain.circle.entity.Circle;
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

@RequiredArgsConstructor
public class UserCircleRepositoryCustomImpl implements UserCircleRepositoryCustom {

    private final JPAQueryFactory query;
    private final EntityManager em;

    @Override
    public List<UserCircle> findAllByUserId(Long userId) {
        QUserCircle uc = new QUserCircle("uc");

        return query
                .select(uc)
                .from(uc)
                .where(uc.user.id.eq(userId))
                .distinct()
                .fetch();
    }

    @Override
    public List<Users> findAllUserByCircleId(Long circleId) {
        QUsers u = new QUsers("u");
        QUserCircle uc = new QUserCircle("uc");

        return query
                .select(uc.user)
                .from(uc)
                .where(uc.circle.id.eq(circleId))
                .join(uc.user, u)
                .fetch();


    }

    @Override
    public List<Circle> findAllCircleByUserId(Long userId) {
        return query
                .select(userCircle.circle)
                .from(userCircle)
                .where(userCircle.user.id.eq(userId))
                .fetch();
    }

    @Override
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    public Long deleteByUserIdAndCircleId(Long userId, Long circleId) {
        QUserCircle uc = new QUserCircle("uc");

        return query
                .delete(uc)
                .where(uc.user.id.eq(userId))
                .where(uc.circle.id.eq(circleId))
                .execute();
    }

}
