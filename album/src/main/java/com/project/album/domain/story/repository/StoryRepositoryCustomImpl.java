package com.project.album.domain.story.repository;

import com.project.album.domain.story.entity.Story;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoryRepositoryCustomImpl implements StoryRepositoryCustom {

    private final EntityManager em;
    @Override
    public List<Story> findAllByCircleId(Long circleId) {
        return em.createQuery("select s from Story as s join fetch CircleStory as CS where CS.circle.id = :circleId", Story.class)
                .setParameter("circleId", circleId)
                .getResultList();

    }
}
