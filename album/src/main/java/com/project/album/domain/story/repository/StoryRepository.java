package com.project.album.domain.story.repository;

import com.project.album.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {


}
