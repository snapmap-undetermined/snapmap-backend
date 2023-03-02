package com.project.domain.comment.repository;

import com.project.domain.comment.entity.PinComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinCommentRepository extends JpaRepository<PinComment, Long>, PinCommentRepositoryCustom {

}
