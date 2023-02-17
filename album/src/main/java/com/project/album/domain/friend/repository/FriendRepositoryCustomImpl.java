package com.project.album.domain.friend.repository;

import com.project.album.domain.friend.entity.Friend;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Friend> findByUserId(Long userId) {
        return em.createQuery("select f from Friend f where f.me.id = :userId", Friend.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
