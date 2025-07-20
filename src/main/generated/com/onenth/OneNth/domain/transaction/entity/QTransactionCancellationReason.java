package com.onenth.OneNth.domain.transaction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransactionCancellationReason is a Querydsl query type for TransactionCancellationReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransactionCancellationReason extends EntityPathBase<TransactionCancellationReason> {

    private static final long serialVersionUID = 1258766919L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransactionCancellationReason transactionCancellationReason = new QTransactionCancellationReason("transactionCancellationReason");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    public final QCancellationReason cancellationReason;

    public final QCancelledTransaction cancelledTransaction;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTransactionCancellationReason(String variable) {
        this(TransactionCancellationReason.class, forVariable(variable), INITS);
    }

    public QTransactionCancellationReason(Path<? extends TransactionCancellationReason> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransactionCancellationReason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransactionCancellationReason(PathMetadata metadata, PathInits inits) {
        this(TransactionCancellationReason.class, metadata, inits);
    }

    public QTransactionCancellationReason(Class<? extends TransactionCancellationReason> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cancellationReason = inits.isInitialized("cancellationReason") ? new QCancellationReason(forProperty("cancellationReason")) : null;
        this.cancelledTransaction = inits.isInitialized("cancelledTransaction") ? new QCancelledTransaction(forProperty("cancelledTransaction")) : null;
    }

}

