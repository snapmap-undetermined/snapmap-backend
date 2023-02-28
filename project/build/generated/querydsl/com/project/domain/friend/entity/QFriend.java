package com.project.domain.friend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriend is a Querydsl query type for Friend
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriend extends EntityPathBase<Friend> {

    private static final long serialVersionUID = -1708373119L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriend friend1 = new QFriend("friend1");

    public final com.project.common.entity.QBaseTimeEntity _super = new com.project.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.project.domain.users.entity.QUsers friend;

    public final StringPath friendName = createString("friendName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.domain.users.entity.QUsers me;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QFriend(String variable) {
        this(Friend.class, forVariable(variable), INITS);
    }

    public QFriend(Path<? extends Friend> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriend(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriend(PathMetadata metadata, PathInits inits) {
        this(Friend.class, metadata, inits);
    }

    public QFriend(Class<? extends Friend> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.friend = inits.isInitialized("friend") ? new com.project.domain.users.entity.QUsers(forProperty("friend"), inits.get("friend")) : null;
        this.me = inits.isInitialized("me") ? new com.project.domain.users.entity.QUsers(forProperty("me"), inits.get("me")) : null;
    }

}

