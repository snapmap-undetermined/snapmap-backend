package com.project.domain.group.repository;

import com.project.domain.group.entity.GroupData;
import com.project.domain.users.entity.Users;
import java.util.List;

public interface GroupRepositoryCustom {

    List<GroupData> findAllGroupByUserId(Long userId);

    List<Users> findAllUserByGroupId(Long groupId);

    GroupData findGroupByKey(String key);

}
