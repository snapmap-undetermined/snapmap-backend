package com.project.domain.pin.repository;

import com.project.domain.pin.entity.Pin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PinRepositoryCustom {
    Page<Pin> findAllByPocketId(Long pocketId, Pageable pageable);

}
