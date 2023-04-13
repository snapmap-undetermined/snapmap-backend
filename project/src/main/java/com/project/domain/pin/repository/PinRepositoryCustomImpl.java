package com.project.domain.pin.repository;

import com.project.domain.pin.entity.Pin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.domain.pin.entity.QPin.pin;


@Repository
@RequiredArgsConstructor
public class PinRepositoryCustomImpl implements PinRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Pin> findAllByPocketId(Long pocketId) {

        return jpaQueryFactory
                .selectFrom(pin)
                .where(pin.pocket.id.eq(pocketId))
                .where(pin.activated.eq(true))
                .fetch();
    }
}
