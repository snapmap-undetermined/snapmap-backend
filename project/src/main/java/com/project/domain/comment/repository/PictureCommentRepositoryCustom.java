package com.project.domain.comment.repository;

import com.project.domain.comment.entity.PictureComment;

import java.util.List;

public interface PictureCommentRepositoryCustom {

    List<PictureComment> findAllByPictureId(Long pictureId);

    PictureComment findByCommentOrder(Long order);

    Long getLastPictureCommentOrder(Long pictureId);

}
