package com.project.domain.usergroup.repository;

import com.project.domain.usergroup.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>, UserGroupRepositoryCustom {

    Optional<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);

}
