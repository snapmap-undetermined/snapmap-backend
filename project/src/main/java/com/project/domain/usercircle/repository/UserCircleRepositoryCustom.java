package com.project.domain.usercircle.repository;

import com.project.domain.circle.entity.Circle;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface UserCircleRepositoryCustom {
    Long deleteByUserIdAndCircleId(Long userId, Long circleId);

}
