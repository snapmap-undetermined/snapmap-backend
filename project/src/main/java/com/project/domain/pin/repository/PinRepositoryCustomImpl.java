package com.project.domain.pin.repository;

import com.project.domain.pin.entity.Pin;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.project.domain.pin.entity.QPin.pin;
import static com.project.domain.pocket.entity.QPocket.pocket;


@Repository
@RequiredArgsConstructor
public class PinRepositoryCustomImpl implements PinRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Pin> findAllByPocketId(Long pocketId) {

        return jpaQueryFactory
                .selectFrom(pin)
                .where(pin.pocket.id.eq(pocketId))
                .fetch();
    }
}
