package com.project.domain.usercircle.repository;

import com.project.domain.circle.entity.Circle;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface UserCircleRepositoryCustom {
    List<Circle> findAllByUserId(Long userId);

    List<Users> findAllUserByCircleId(Long circleId);

    List<Circle> findAllCircleByUserId(Long userId);

    Long deleteByUserIdAndCircleId(Long userId, Long circleId);

}
