package com.project.album.domain.circle.repository;

import com.project.album.domain.circle.entity.Circle;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CircleRepositoryCustomImpl implements CircleRepositoryCustom {

    private final EntityManager em;

    @Override
    public Circle findByCircleKey(String circleKey) {
        return em.createQuery("select c From Circle c where c.circleKey = :circleKey", Circle.class)
                .setParameter("circleKey", circleKey)
                .getSingleResult();

    }
}
