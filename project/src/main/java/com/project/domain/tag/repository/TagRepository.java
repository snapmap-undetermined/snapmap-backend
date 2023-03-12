package com.project.domain.tag.repository;

import com.project.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {

    Optional<Tag> findByName(String tagName);
}
