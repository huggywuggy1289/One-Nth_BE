package com.onenth.OneNth.domain.transaction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransaction is a Querydsl query type for Transaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransaction extends EntityPathBase<Transaction> {

    private static final long serialVersionUID = 435801344L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransaction transaction = new QTransaction("transaction");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    public final com.onenth.OneNth.domain.chat.entity.QChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.onenth.OneNth.domain.product.entity.QPurchaseItem purchaseItem;

    public final com.onenth.OneNth.domain.product.entity.QSharingItem sharingItem;

    public final QTransactionConfirmation transactionConfirmation;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTransaction(String variable) {
        this(Transaction.class, forVariable(variable), INITS);
    }

    public QTransaction(Path<? extends Transaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransaction(PathMetadata metadata, PathInits inits) {
        this(Transaction.class, metadata, inits);
    }

    public QTransaction(Class<? extends Transaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.onenth.OneNth.domain.chat.entity.QChatRoom(forProperty("chatRoom")) : null;
        this.purchaseItem = inits.isInitialized("purchaseItem") ? new com.onenth.OneNth.domain.product.entity.QPurchaseItem(forProperty("purchaseItem"), inits.get("purchaseItem")) : null;
        this.sharingItem = inits.isInitialized("sharingItem") ? new com.onenth.OneNth.domain.product.entity.QSharingItem(forProperty("sharingItem"), inits.get("sharingItem")) : null;
        this.transactionConfirmation = inits.isInitialized("transactionConfirmation") ? new QTransactionConfirmation(forProperty("transactionConfirmation"), inits.get("transactionConfirmation")) : null;
    }

}

