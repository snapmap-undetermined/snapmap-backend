package com.project.domain.pinpicture.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPinPicture is a Querydsl query type for PinPicture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPinPicture extends EntityPathBase<PinPicture> {

    private static final long serialVersionUID = -1207988361L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPinPicture pinPicture = new QPinPicture("pinPicture");

    public final com.project.common.entity.QBaseTimeEntity _super = new com.project.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.project.domain.picture.entity.QPicture picture;

    public final com.project.domain.pin.entity.QPin pin;

    public QPinPicture(String variable) {
        this(PinPicture.class, forVariable(variable), INITS);
    }

    public QPinPicture(Path<? extends PinPicture> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPinPicture(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPinPicture(PathMetadata metadata, PathInits inits) {
        this(PinPicture.class, metadata, inits);
    }

    public QPinPicture(Class<? extends PinPicture> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.picture = inits.isInitialized("picture") ? new com.project.domain.picture.entity.QPicture(forProperty("picture"), inits.get("picture")) : null;
        this.pin = inits.isInitialized("pin") ? new com.project.domain.pin.entity.QPin(forProperty("pin"), inits.get("pin")) : null;
    }

}

