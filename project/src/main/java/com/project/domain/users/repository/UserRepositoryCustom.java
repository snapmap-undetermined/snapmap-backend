package com.project.domain.users.repository;


import com.project.domain.users.entity.Users;

import java.util.List;

public interface UserRepositoryCustom {

    List<Users> findAllUserByCircleId(Long circleId);

}
