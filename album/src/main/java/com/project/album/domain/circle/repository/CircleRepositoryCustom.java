package com.project.album.domain.circle.repository;

import com.project.album.domain.circle.entity.Circle;


public interface CircleRepositoryCustom {

    Circle findByCircleKey(String circleKey);
}
