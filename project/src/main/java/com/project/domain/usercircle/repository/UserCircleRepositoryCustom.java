package com.project.domain.usercircle.repository;

import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface UserCircleRepositoryCustom {
    List<UserCircle> findByUserId(Long userId);

    List<Users> findAllUserByCircleId(Long circleId);

    Long deleteByUserIdAndCircleId(Long userId, Long circleId);

}
