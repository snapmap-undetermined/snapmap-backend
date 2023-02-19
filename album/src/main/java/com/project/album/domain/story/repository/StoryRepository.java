package com.project.album.domain.story.repository;

import com.project.album.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {

    List<Story> findByUserId(Long userId);

}
