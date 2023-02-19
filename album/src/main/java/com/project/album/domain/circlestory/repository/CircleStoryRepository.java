package com.project.album.domain.circlestory.repository;

import com.project.album.domain.circlestory.entity.CircleStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleStoryRepository extends JpaRepository<CircleStory, Long>, CircleStoryRepositoryCustom {


}
