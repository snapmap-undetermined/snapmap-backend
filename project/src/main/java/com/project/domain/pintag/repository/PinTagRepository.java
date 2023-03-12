package com.project.domain.pintag.repository;

import com.project.domain.pintag.entity.PinTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinTagRepository extends JpaRepository<PinTag, Long>, PinTagRepositoryCustom {

}
