package com.project.domain.circle.repository;


import com.project.domain.circle.entity.Circle;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface CircleRepositoryCustom {

    List<Circle> findAllCircleByUserId(Long userId);

    List<Users> findAllUserByCircleId(Long circleId);

    Circle findCircleByKey(String key);

}
