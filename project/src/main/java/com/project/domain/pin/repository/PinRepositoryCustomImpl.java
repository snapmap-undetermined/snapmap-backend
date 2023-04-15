package com.project.domain.pin.repository;

import com.project.domain.pin.entity.Pin;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

import static com.project.domain.pin.entity.QPin.pin;
import static com.project.domain.pocket.entity.QPocket.pocket;


@Repository
@RequiredArgsConstructor
public class PinRepositoryCustomImpl implements PinRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Pin> findAllByPocketId(Long pocketId, Pageable pageable) {
        List<Pin> content = jpaQueryFactory
                .selectFrom(pin)
                .where(isPocketIdEquals(pocketId))
                .orderBy(pin.createdAt.desc()) // 최신 순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(pin.count())
                .from(pin)
                .where(isPocketIdEquals(pocketId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }

    private BooleanExpression isPocketIdEquals(Long pocketId) {
        return pocketId == null ? null : pocket.id.eq(pocketId);
    }
}
