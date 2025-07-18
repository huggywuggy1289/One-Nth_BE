package com.onenth.OneNth.domain.transaction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransactionConfirmation is a Querydsl query type for TransactionConfirmation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransactionConfirmation extends EntityPathBase<TransactionConfirmation> {

    private static final long serialVersionUID = 1603808597L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransactionConfirmation transactionConfirmation = new QTransactionConfirmation("transactionConfirmation");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAccepted = createBoolean("isAccepted");

    public final StringPath trackingNumber = createString("trackingNumber");

    public final DateTimePath<java.time.LocalDateTime> tradeDate = createDateTime("tradeDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> tradePrice = createNumber("tradePrice", Integer.class);

    public final EnumPath<com.onenth.OneNth.domain.transaction.entity.enums.TradeType> tradeType = createEnum("tradeType", com.onenth.OneNth.domain.transaction.entity.enums.TradeType.class);

    public final QTransaction transaction;

    public final QTransactionCompletion transactionCompletion;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTransactionConfirmation(String variable) {
        this(TransactionConfirmation.class, forVariable(variable), INITS);
    }

    public QTransactionConfirmation(Path<? extends TransactionConfirmation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransactionConfirmation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransactionConfirmation(PathMetadata metadata, PathInits inits) {
        this(TransactionConfirmation.class, metadata, inits);
    }

    public QTransactionConfirmation(Class<? extends TransactionConfirmation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.transaction = inits.isInitialized("transaction") ? new QTransaction(forProperty("transaction"), inits.get("transaction")) : null;
        this.transactionCompletion = inits.isInitialized("transactionCompletion") ? new QTransactionCompletion(forProperty("transactionCompletion"), inits.get("transactionCompletion")) : null;
    }

}

