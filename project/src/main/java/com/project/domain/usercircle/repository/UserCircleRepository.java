package com.project.domain.usercircle.repository;

import com.project.domain.usercircle.entity.UserCircle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCircleRepository extends JpaRepository<UserCircle, Long>, UserCircleRepositoryCustom {

    Optional<UserCircle> findByUserIdAndCircleId(Long userId, Long circleId);

}
