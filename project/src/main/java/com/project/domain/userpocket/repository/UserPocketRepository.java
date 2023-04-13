package com.project.domain.userpocket.repository;

import com.project.domain.userpocket.entity.UserPocket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPocketRepository extends JpaRepository<UserPocket, Long>, UserPocketRepositoryCustom {

    Optional<UserPocket> findByUserIdAndPocketId(Long userId, Long pocketId);

}
