package com.onenth.OneNth.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberNotificationSetting is a Querydsl query type for MemberNotificationSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberNotificationSetting extends EntityPathBase<MemberNotificationSetting> {

    private static final long serialVersionUID = 979123767L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberNotificationSetting memberNotificationSetting = new QMemberNotificationSetting("memberNotificationSetting");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    public final BooleanPath chatNotifications = createBoolean("chatNotifications");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final BooleanPath reviewNotifications = createBoolean("reviewNotifications");

    public final BooleanPath scrapNotifications = createBoolean("scrapNotifications");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberNotificationSetting(String variable) {
        this(MemberNotificationSetting.class, forVariable(variable), INITS);
    }

    public QMemberNotificationSetting(Path<? extends MemberNotificationSetting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberNotificationSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberNotificationSetting(PathMetadata metadata, PathInits inits) {
        this(MemberNotificationSetting.class, metadata, inits);
    }

    public QMemberNotificationSetting(Class<? extends MemberNotificationSetting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

