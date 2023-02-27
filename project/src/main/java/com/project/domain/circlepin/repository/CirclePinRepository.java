package com.project.domain.circlepin.repository;

import com.project.domain.circlepin.entity.CirclePin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CirclePinRepository extends JpaRepository<CirclePin, Long>, CirclePinRepositoryCustom {

    List<CirclePin> findAllByPinId(Long pinId);
}
