package com.project.domain.usercircle.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserCircle is a Querydsl query type for UserCircle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCircle extends EntityPathBase<UserCircle> {

    private static final long serialVersionUID = -943417253L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserCircle userCircle = new QUserCircle("userCircle");

    public final com.project.common.entity.QBaseTimeEntity _super = new com.project.common.entity.QBaseTimeEntity(this);

    public final com.project.domain.circle.entity.QCircle circle;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.project.domain.users.entity.QUsers user;

    public QUserCircle(String variable) {
        this(UserCircle.class, forVariable(variable), INITS);
    }

    public QUserCircle(Path<? extends UserCircle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserCircle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserCircle(PathMetadata metadata, PathInits inits) {
        this(UserCircle.class, metadata, inits);
    }

    public QUserCircle(Class<? extends UserCircle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.circle = inits.isInitialized("circle") ? new com.project.domain.circle.entity.QCircle(forProperty("circle")) : null;
        this.user = inits.isInitialized("user") ? new com.project.domain.users.entity.QUsers(forProperty("user"), inits.get("user")) : null;
    }

}

