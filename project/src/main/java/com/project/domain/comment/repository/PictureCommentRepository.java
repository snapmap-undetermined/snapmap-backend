package com.project.domain.comment.repository;

import com.project.domain.comment.entity.PictureComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureCommentRepository extends JpaRepository<PictureComment, Long>, PictureCommentRepositoryCustom {
}
