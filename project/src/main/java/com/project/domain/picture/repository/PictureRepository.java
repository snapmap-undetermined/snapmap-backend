package com.project.domain.picture.repository;

import com.project.domain.picture.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long>, PictureRepositoryCustom{

    List<Picture> findAllByStoryId(Long storyId);

}
