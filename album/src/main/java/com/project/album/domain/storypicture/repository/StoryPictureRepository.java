package com.project.album.domain.storypicture.repository;

import com.project.album.domain.story.entity.Story;
import com.project.album.domain.story.repository.StoryRepositoryCustom;
import com.project.album.domain.storypicture.entity.StoryPicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryPictureRepository extends JpaRepository<StoryPicture, Long>, StoryRepositoryCustom {


}
