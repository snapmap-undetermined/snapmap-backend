package com.project.domain.pin.repository;

import com.project.domain.pin.entity.Pin;

import java.util.List;

public interface PinRepositoryCustom {
    List<Pin> findAllByCircleId(Long circleId);

}
