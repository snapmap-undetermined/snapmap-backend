package com.project.domain.comment.repository;


import com.project.domain.comment.entity.PictureComment;
import com.project.domain.comment.entity.QPictureComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.project.domain.comment.entity.QPictureComment.pictureComment;

@RequiredArgsConstructor
public class PictureCommentRepositoryCustomImpl implements PictureCommentRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public List<PictureComment> findAllByPictureId(Long pictureId) {
        return query
                .selectFrom(pictureComment)
                .where(pictureComment.picture.id.eq(pictureId))
                .fetch();
    }

    @Override
    public PictureComment findByCommentOrder(Long order) {
        return query
                .selectFrom(pictureComment)
                .where(pictureComment.commentOrder.eq(order))
                .fetchOne();

    }

    @Override
    public Long getLastPictureCommentOrder(Long pictureId) {
        return query
                .select(pictureComment.commentOrder.max().coalesce(0L))
                .where(pictureComment.picture.id.eq(pictureId))
                .from(pictureComment)
                .fetchOne();
    }
}
