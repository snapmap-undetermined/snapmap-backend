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

import static com.project.domain.comment.entity.QPinComment.pinComment;

@RequiredArgsConstructor
public class PinCommentRepositoryCustomImpl implements PinCommentRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<PinComment> findAllByPinId(Long pinId) {
        return query
                .selectFrom(pinComment)
                .where(pinComment.pin.id.eq(pinId))
                .fetch();
    }

    @Override
    public PinComment findByCommentOrder(Long order) {
        return query
                .selectFrom(pinComment)
                .where(pinComment.commentOrder.eq(order))
                .fetchOne();

    }

    @Override
    public Long getLastPinCommentOrder(Long pinId) {
        return query
                .select(pinComment.commentOrder.max().coalesce(0L))
                .where(pinComment.pin.id.eq(pinId))
                .from(pinComment)
                .fetchOne();
    }

}
