package com.onenth.OneNth.domain.deal.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeal is a Querydsl query type for Deal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeal extends EntityPathBase<Deal> {

    private static final long serialVersionUID = 401087154L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeal deal = new QDeal("deal");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDealConfirmation dealConfirmation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.onenth.OneNth.domain.product.entity.QPurchaseItem purchaseItem;

    public final com.onenth.OneNth.domain.product.entity.QSharingItem sharingItem;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDeal(String variable) {
        this(Deal.class, forVariable(variable), INITS);
    }

    public QDeal(Path<? extends Deal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeal(PathMetadata metadata, PathInits inits) {
        this(Deal.class, metadata, inits);
    }

    public QDeal(Class<? extends Deal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dealConfirmation = inits.isInitialized("dealConfirmation") ? new QDealConfirmation(forProperty("dealConfirmation"), inits.get("dealConfirmation")) : null;
        this.purchaseItem = inits.isInitialized("purchaseItem") ? new com.onenth.OneNth.domain.product.entity.QPurchaseItem(forProperty("purchaseItem"), inits.get("purchaseItem")) : null;
        this.sharingItem = inits.isInitialized("sharingItem") ? new com.onenth.OneNth.domain.product.entity.QSharingItem(forProperty("sharingItem"), inits.get("sharingItem")) : null;
    }

}

