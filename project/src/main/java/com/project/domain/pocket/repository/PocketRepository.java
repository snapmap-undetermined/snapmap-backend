package com.project.domain.pocket.repository;

import com.project.domain.pocket.entity.Pocket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PocketRepository extends JpaRepository<Pocket, Long>, PocketRepositoryCustom {

}
