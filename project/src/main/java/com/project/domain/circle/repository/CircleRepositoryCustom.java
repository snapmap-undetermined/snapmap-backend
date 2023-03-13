package com.project.domain.circle.repository;


import com.project.domain.circle.entity.Circle;

import java.util.List;

public interface CircleRepositoryCustom {

    List<Circle> findAllCircleByUserId(Long userId);
}
