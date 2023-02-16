package com.project.album.domain.usercircle.repository;

import com.project.album.domain.usercircle.entity.UserCircle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserCircle, Long>, UserGroupRepositoryCustom {


}
