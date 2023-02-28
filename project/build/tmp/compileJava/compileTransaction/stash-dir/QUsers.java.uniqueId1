package com.project.domain.users.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = -11338773L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsers users = new QUsers("users");

    public final com.project.common.entity.QBaseTimeEntity _super = new com.project.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final ListPath<com.project.domain.friend.entity.Friend, com.project.domain.friend.entity.QFriend> friendList = this.<com.project.domain.friend.entity.Friend, com.project.domain.friend.entity.QFriend>createList("friendList", com.project.domain.friend.entity.Friend.class, com.project.domain.friend.entity.QFriend.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<com.project.domain.pin.entity.Pin, com.project.domain.pin.entity.QPin> pinList = this.<com.project.domain.pin.entity.Pin, com.project.domain.pin.entity.QPin>createList("pinList", com.project.domain.pin.entity.Pin.class, com.project.domain.pin.entity.QPin.class, PathInits.DIRECT2);

    public final StringPath profileImage = createString("profileImage");

    public final QRefreshToken refreshToken;

    public final EnumPath<com.project.common.entity.Role> role = createEnum("role", com.project.common.entity.Role.class);

    public QUsers(String variable) {
        this(Users.class, forVariable(variable), INITS);
    }

    public QUsers(Path<? extends Users> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsers(PathMetadata metadata, PathInits inits) {
        this(Users.class, metadata, inits);
    }

    public QUsers(Class<? extends Users> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.refreshToken = inits.isInitialized("refreshToken") ? new QRefreshToken(forProperty("refreshToken")) : null;
    }

}

