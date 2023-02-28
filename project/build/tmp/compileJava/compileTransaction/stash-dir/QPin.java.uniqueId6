package com.project.domain.pin.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPin is a Querydsl query type for Pin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPin extends EntityPathBase<Pin> {

    private static final long serialVersionUID = -49014645L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPin pin = new QPin("pin");

    public final com.project.common.entity.QBaseTimeEntity _super = new com.project.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ComparablePath<org.locationtech.jts.geom.Point> location = createComparable("location", org.locationtech.jts.geom.Point.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<com.project.domain.picture.entity.Picture, com.project.domain.picture.entity.QPicture> pictureList = this.<com.project.domain.picture.entity.Picture, com.project.domain.picture.entity.QPicture>createList("pictureList", com.project.domain.picture.entity.Picture.class, com.project.domain.picture.entity.QPicture.class, PathInits.DIRECT2);

    public final com.project.domain.users.entity.QUsers user;

    public QPin(String variable) {
        this(Pin.class, forVariable(variable), INITS);
    }

    public QPin(Path<? extends Pin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPin(PathMetadata metadata, PathInits inits) {
        this(Pin.class, metadata, inits);
    }

    public QPin(Class<? extends Pin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.project.domain.users.entity.QUsers(forProperty("user"), inits.get("user")) : null;
    }

}

