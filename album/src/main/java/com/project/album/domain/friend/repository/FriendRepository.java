package com.project.album.domain.friend.repository;

import com.project.album.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {

}
