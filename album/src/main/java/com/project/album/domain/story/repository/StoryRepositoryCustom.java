package com.project.album.domain.story.repository;

import com.project.album.domain.story.entity.Story;

import java.util.List;

public interface StoryRepositoryCustom {
    List<Story> findAllByCircleId(Long circleId);

}
