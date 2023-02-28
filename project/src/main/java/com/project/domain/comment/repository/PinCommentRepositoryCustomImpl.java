package com.project.domain.comment.repository;

import com.project.domain.comment.entity.PinComment;
import com.project.domain.comment.entity.QPinComment;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PinCommentRepositoryCustomImpl implements PinCommentRepositoryCustom {

    private final JPAQueryFactory query;
    private final EntityManager em;

    @Override
    public List<PinComment> findByPinId(Long pinId) {
        QPinComment pc = new QPinComment("pc");

        return query
                .select(pc)
                .from(pc)
                .where(pc.pin.id.eq(pinId))
                .fetch();
    }

    @Override
    public PinComment findByCommentOrder(Long order) {
        QPinComment pc = new QPinComment("pc");

        return query
                .select(pc)
                .from(pc)
                .where(pc.commentOrder.eq(order))
                .fetch().get(0);

    }

    @Override
    public Long getLastPinCommentOrder(Long pinId) {
        QPinComment pc = new QPinComment("pc");
        return query
                .select(pc.commentOrder.max().coalesce(0L))
                .where(pc.pin.id.eq(pinId))
                .from(pc)
                .fetchFirst();
    }

}
