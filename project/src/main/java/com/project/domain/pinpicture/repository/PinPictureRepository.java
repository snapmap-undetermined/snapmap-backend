package com.project.domain.pinpicture.repository;

import com.project.domain.pin.repository.PinRepositoryCustom;
import com.project.domain.pinpicture.entity.PinPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinPictureRepository extends JpaRepository<PinPicture, Long>, PinRepositoryCustom {

    List<PinPicture> findAllByPinId(Long pinId);
}
