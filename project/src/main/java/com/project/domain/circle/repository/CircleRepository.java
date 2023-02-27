package com.project.domain.circle.repository;

import com.project.domain.circle.entity.Circle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleRepository extends JpaRepository<Circle, Long>, CircleRepositoryCustom {

}
