package com.project.album.domain.circle.repository;

import com.project.album.domain.circle.entity.Circle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleRepository extends JpaRepository<Circle, Long>, CircleRepositoryCustom {

}
