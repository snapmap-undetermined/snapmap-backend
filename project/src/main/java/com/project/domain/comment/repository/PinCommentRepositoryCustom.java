package com.project.domain.comment.repository;

import com.project.domain.comment.entity.PinComment;

import java.util.List;

public interface PinCommentRepositoryCustom {

    List<PinComment> findByPinId(Long pinId);

    PinComment findByCommentOrder(Long order);

    Long getLastPinCommentOrder(Long pinId);
}
