package com.project.domain.pin.repository;

import com.project.domain.pin.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long>, PinRepositoryCustom {

    List<Pin> findByUserId(Long userId);

}
