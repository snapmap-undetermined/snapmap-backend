package com.project.domain.users.repository;

import com.project.domain.usercircle.repository.UserCircleRepositoryCustom;
import com.project.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>, UserRepositoryCustom {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByNickname(String nickname);
}
