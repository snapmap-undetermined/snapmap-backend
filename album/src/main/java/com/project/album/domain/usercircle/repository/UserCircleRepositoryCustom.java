package com.project.album.domain.usercircle.repository;

import com.project.album.domain.usercircle.entity.UserCircle;
import com.project.album.domain.users.entity.Users;

import java.util.List;

public interface UserCircleRepositoryCustom {
    List<UserCircle> findByUserId(Long userId);

    List<Users> findAllByCircleId(Long userId, Long circleId);

    int deleteByUserIdAndCircleId(Long userId, Long circleId);

}
