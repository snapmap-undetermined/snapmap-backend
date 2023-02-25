package com.project.domain.story.repository;

import com.project.domain.story.entity.Story;

import java.util.List;

public interface StoryRepositoryCustom {
    List<Story> findAllByCircleId(Long circleId);

}
