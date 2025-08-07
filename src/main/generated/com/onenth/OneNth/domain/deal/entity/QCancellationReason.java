package com.onenth.OneNth.domain.deal.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCancellationReason is a Querydsl query type for CancellationReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCancellationReason extends EntityPathBase<CancellationReason> {

    private static final long serialVersionUID = 1739498733L;

    public static final QCancellationReason cancellationReason = new QCancellationReason("cancellationReason");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<DealCancellationReason, QDealCancellationReason> dealCancellationReasonList = this.<DealCancellationReason, QDealCancellationReason>createList("dealCancellationReasonList", DealCancellationReason.class, QDealCancellationReason.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reasonText = createString("reasonText");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCancellationReason(String variable) {
        super(CancellationReason.class, forVariable(variable));
    }

    public QCancellationReason(Path<? extends CancellationReason> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCancellationReason(PathMetadata metadata) {
        super(CancellationReason.class, metadata);
    }

}

