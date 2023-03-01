package com.project.domain.location.repository;

import com.project.domain.location.dto.LocationDTO;
import com.project.domain.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
