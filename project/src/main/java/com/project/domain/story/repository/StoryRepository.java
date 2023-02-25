package com.project.domain.story.repository;

import com.project.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {

    List<Story> findByUserId(Long userId);

}
