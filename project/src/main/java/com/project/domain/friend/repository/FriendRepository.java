package com.project.domain.friend.repository;

import com.project.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {
    Optional<Friend> findByMeIdAndMateId(Long meId, Long mateId);

}
