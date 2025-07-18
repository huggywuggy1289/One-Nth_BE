package com.onenth.OneNth.domain.transaction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransactionCompletion is a Querydsl query type for TransactionCompletion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransactionCompletion extends EntityPathBase<TransactionCompletion> {

    private static final long serialVersionUID = 812469500L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransactionCompletion transactionCompletion = new QTransactionCompletion("transactionCompletion");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAccepted = createBoolean("isAccepted");

    public final StringPath trackingNumber = createString("trackingNumber");

    public final DateTimePath<java.time.LocalDateTime> tradeDate = createDateTime("tradeDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> tradePrice = createNumber("tradePrice", Integer.class);

    public final EnumPath<com.onenth.OneNth.domain.transaction.entity.enums.TradeType> tradeType = createEnum("tradeType", com.onenth.OneNth.domain.transaction.entity.enums.TradeType.class);

    public final QTransactionConfirmation transactionConfirmation;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTransactionCompletion(String variable) {
        this(TransactionCompletion.class, forVariable(variable), INITS);
    }

    public QTransactionCompletion(Path<? extends TransactionCompletion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransactionCompletion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransactionCompletion(PathMetadata metadata, PathInits inits) {
        this(TransactionCompletion.class, metadata, inits);
    }

    public QTransactionCompletion(Class<? extends TransactionCompletion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.transactionConfirmation = inits.isInitialized("transactionConfirmation") ? new QTransactionConfirmation(forProperty("transactionConfirmation"), inits.get("transactionConfirmation")) : null;
    }

}

