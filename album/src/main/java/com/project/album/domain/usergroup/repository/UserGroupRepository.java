package com.project.album.domain.usergroup.repository;

import com.project.album.domain.story.entity.Story;
import com.project.album.domain.usergroup.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>, UserGroupRepositoryCustom {


}
