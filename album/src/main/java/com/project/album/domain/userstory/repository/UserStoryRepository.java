package com.project.album.domain.userstory.repository;

import com.project.album.domain.userstory.entity.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStory, Long>, UserStoryRepositoryCustom {


}
