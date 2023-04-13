package com.project.domain.pocket.repository;


import com.project.domain.pocket.entity.Pocket;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface PocketRepositoryCustom {

    List<Pocket> findAllPocketByUserId(Long userId);

    List<Users> findAllUserByPocketId(Long pocket);

    Pocket findPocketByKey(String key);

}
