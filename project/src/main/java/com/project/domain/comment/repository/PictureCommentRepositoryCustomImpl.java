package com.project.domain.comment.repository;


import com.project.domain.comment.entity.PictureComment;
import com.project.domain.comment.entity.QPictureComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PictureCommentRepositoryCustomImpl implements PictureCommentRepositoryCustom{

    private final JPAQueryFactory query;
    private final EntityManager em;

    @Override
    public List<PictureComment> findByPictureId(Long pictureId) {
        QPictureComment pc = new QPictureComment("pc");

        return query
                .select(pc)
                .from(pc)
                .where(pc.picture.id.eq(pictureId))
                .fetch();
    }

    @Override
    public PictureComment findByCommentOrder(Long order) {
        QPictureComment pc = new QPictureComment("pc");

        return query
                .select(pc)
                .from(pc)
                .where(pc.commentOrder.eq(order))
                .fetch().get(0);

    }

    @Override
    public Long getLastPictureCommentOrder(Long pictureId) {
        QPictureComment pc = new QPictureComment("pc");
        return query
                .select(pc.commentOrder.max().coalesce(0L))
                .where(pc.picture.id.eq(pictureId))
                .from(pc)
                .fetchOne();
    }
}
