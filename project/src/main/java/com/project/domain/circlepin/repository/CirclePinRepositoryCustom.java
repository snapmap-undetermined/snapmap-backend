package com.project.domain.circlepin.repository;

import com.project.domain.circle.entity.Circle;
import com.project.domain.pin.entity.Pin;

import java.util.List;

public interface CirclePinRepositoryCustom {

    List<Circle> findAllCirclesByPinId(Long pinId);

    List<Pin> findAllPinsByCircleId(Long circleId);
}
