package com.project.album.domain.picture.repository;

import com.project.album.domain.picture.entity.Picture;
import com.project.album.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long>, PictureRepositoryCustom{

    List<Picture> findAllByStoryId(Long storyId);

}
