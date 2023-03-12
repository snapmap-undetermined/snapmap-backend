package com.project.domain.circlepin.repository;

import com.project.domain.circle.entity.Circle;
import com.project.domain.pin.entity.Pin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.domain.circlepin.entity.QCirclePin.circlePin;

@Repository
@RequiredArgsConstructor
public class CirclePinRepositoryCustomImpl implements CirclePinRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Circle> findAllCirclesByPinId(Long pinId) {

        return jpaQueryFactory
                .select(circlePin.circle)
                .from(circlePin)
                .where(circlePin.pin.id.eq(pinId))
                .fetch();
    }

    @Override
    public List<Pin> findAllPinsByCircleId(Long circleId) {
        return jpaQueryFactory
                .select(circlePin.pin)
                .from(circlePin)
                .where(circlePin.circle.id.eq(circleId))
                .fetch();
    }
}
