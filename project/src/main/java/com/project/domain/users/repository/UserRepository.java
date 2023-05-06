package com.project.domain.users.repository;

import com.project.domain.users.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>, UserRepositoryCustom{

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByNickname(String nickname);
}
