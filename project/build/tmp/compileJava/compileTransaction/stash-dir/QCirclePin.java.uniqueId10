package com.project.domain.circlepin.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCirclePin is a Querydsl query type for CirclePin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCirclePin extends EntityPathBase<CirclePin> {

    private static final long serialVersionUID = -799918101L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCirclePin circlePin = new QCirclePin("circlePin");

    public final com.project.common.entity.QBaseTimeEntity _super = new com.project.common.entity.QBaseTimeEntity(this);

    public final com.project.domain.circle.entity.QCircle circle;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.project.domain.pin.entity.QPin pin;

    public QCirclePin(String variable) {
        this(CirclePin.class, forVariable(variable), INITS);
    }

    public QCirclePin(Path<? extends CirclePin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCirclePin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCirclePin(PathMetadata metadata, PathInits inits) {
        this(CirclePin.class, metadata, inits);
    }

    public QCirclePin(Class<? extends CirclePin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.circle = inits.isInitialized("circle") ? new com.project.domain.circle.entity.QCircle(forProperty("circle")) : null;
        this.pin = inits.isInitialized("pin") ? new com.project.domain.pin.entity.QPin(forProperty("pin"), inits.get("pin")) : null;
    }

}

