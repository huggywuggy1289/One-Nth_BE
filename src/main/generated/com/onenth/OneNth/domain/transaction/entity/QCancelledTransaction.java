package com.onenth.OneNth.domain.transaction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCancelledTransaction is a Querydsl query type for CancelledTransaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCancelledTransaction extends EntityPathBase<CancelledTransaction> {

    private static final long serialVersionUID = 188043499L;

    public static final QCancelledTransaction cancelledTransaction = new QCancelledTransaction("cancelledTransaction");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<TransactionCancellationReason, QTransactionCancellationReason> transactionCancellationReasonList = this.<TransactionCancellationReason, QTransactionCancellationReason>createList("transactionCancellationReasonList", TransactionCancellationReason.class, QTransactionCancellationReason.class, PathInits.DIRECT2);

    public final NumberPath<Long> transactionId = createNumber("transactionId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCancelledTransaction(String variable) {
        super(CancelledTransaction.class, forVariable(variable));
    }

    public QCancelledTransaction(Path<? extends CancelledTransaction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCancelledTransaction(PathMetadata metadata) {
        super(CancelledTransaction.class, metadata);
    }

}

