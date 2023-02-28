package com.project.domain.circle.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCircle is a Querydsl query type for Circle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCircle extends EntityPathBase<Circle> {

    private static final long serialVersionUID = -2127237019L;

    public static final QCircle circle = new QCircle("circle");

    public final com.project.common.entity.QBaseTimeEntity _super = new com.project.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public QCircle(String variable) {
        super(Circle.class, forVariable(variable));
    }

    public QCircle(Path<? extends Circle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCircle(PathMetadata metadata) {
        super(Circle.class, metadata);
    }

}

