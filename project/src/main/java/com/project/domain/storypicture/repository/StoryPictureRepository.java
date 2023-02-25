package com.project.domain.storypicture.repository;

import com.project.domain.story.repository.StoryRepositoryCustom;
import com.project.domain.storypicture.entity.StoryPicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryPictureRepository extends JpaRepository<StoryPicture, Long>, StoryRepositoryCustom {


}
