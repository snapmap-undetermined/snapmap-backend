package com.project.domain.circlestory.repository;

import com.project.domain.circlestory.entity.CircleStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleStoryRepository extends JpaRepository<CircleStory, Long>, CircleStoryRepositoryCustom {


}
