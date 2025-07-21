package com.onenth.OneNth.domain.alert.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKeywordAlert is a Querydsl query type for KeywordAlert
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKeywordAlert extends EntityPathBase<KeywordAlert> {

    private static final long serialVersionUID = -188038961L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKeywordAlert keywordAlert = new QKeywordAlert("keywordAlert");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath enabled = createBoolean("enabled");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public final com.onenth.OneNth.domain.member.entity.QMember member;

    public final com.onenth.OneNth.domain.region.entity.QRegion regionKeyword;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QKeywordAlert(String variable) {
        this(KeywordAlert.class, forVariable(variable), INITS);
    }

    public QKeywordAlert(Path<? extends KeywordAlert> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKeywordAlert(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKeywordAlert(PathMetadata metadata, PathInits inits) {
        this(KeywordAlert.class, metadata, inits);
    }

    public QKeywordAlert(Class<? extends KeywordAlert> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.onenth.OneNth.domain.member.entity.QMember(forProperty("member")) : null;
        this.regionKeyword = inits.isInitialized("regionKeyword") ? new com.onenth.OneNth.domain.region.entity.QRegion(forProperty("regionKeyword")) : null;
    }

}

