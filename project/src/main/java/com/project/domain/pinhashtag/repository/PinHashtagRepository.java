package com.project.domain.pinhashtag.repository;

import com.project.domain.pinhashtag.entity.PinHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinHashtagRepository extends JpaRepository<PinHashtag, Long>, PinHashtagRepositoryCustom {

}
