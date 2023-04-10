package com.project.domain.group.repository;

import com.project.domain.group.entity.Groups;
import com.project.domain.users.entity.Users;
import java.util.List;

public interface GroupRepositoryCustom {

    List<Groups> findAllGroupByUserId(Long userId);

    List<Users> findAllUserByGroupId(Long groupId);

    Groups findGroupByKey(String key);

}
