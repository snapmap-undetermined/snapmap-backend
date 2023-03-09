package com.project.domain.pin.repository;

import com.project.domain.pin.entity.Pin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.domain.circlepin.entity.QCirclePin.circlePin;

@Repository
@RequiredArgsConstructor
public class PinRepositoryCustomImpl implements PinRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Pin> findAllByCircleId(Long circleId) {

        return query
                .select(circlePin.pin)
                .from(circlePin)
                .where(circlePin.circle.id.eq(circleId))
                .fetch();
    }

    @Override
    public int pinTotalCountByCircleId(Long circleId) {
        return Math.toIntExact(query
                .select(circlePin.count())
                .from(circlePin)
                .where(circlePin.circle.id.eq(circleId))
                .fetchFirst()
        );
    }


}
