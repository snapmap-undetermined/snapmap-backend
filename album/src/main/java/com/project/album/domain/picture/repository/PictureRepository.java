package com.project.album.domain.picture.repository;

import com.project.album.domain.picture.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long>, PictureRepositoryCustom{

}
