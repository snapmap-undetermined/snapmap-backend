package com.project.domain.group.repository;

import com.project.domain.group.entity.GroupData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupData, Long>, GroupRepositoryCustom {

}
