package com.project.domain.pinpicture.repository;

import com.project.domain.picture.entity.Picture;

import java.util.List;

public interface PinPictureRepositoryCustom {

    List<Picture> findAllPicturesByPinId(Long pinId);
}
