package com.onenth.OneNth.domain.deal.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDealCancellationReason is a Querydsl query type for DealCancellationReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDealCancellationReason extends EntityPathBase<DealCancellationReason> {

    private static final long serialVersionUID = 693389177L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDealCancellationReason dealCancellationReason = new QDealCancellationReason("dealCancellationReason");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    public final QCancellationReason cancellationReason;

    public final QCancelledDeal cancelledDeal;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDealCancellationReason(String variable) {
        this(DealCancellationReason.class, forVariable(variable), INITS);
    }

    public QDealCancellationReason(Path<? extends DealCancellationReason> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDealCancellationReason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDealCancellationReason(PathMetadata metadata, PathInits inits) {
        this(DealCancellationReason.class, metadata, inits);
    }

    public QDealCancellationReason(Class<? extends DealCancellationReason> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cancellationReason = inits.isInitialized("cancellationReason") ? new QCancellationReason(forProperty("cancellationReason")) : null;
        this.cancelledDeal = inits.isInitialized("cancelledDeal") ? new QCancelledDeal(forProperty("cancelledDeal")) : null;
    }

}

