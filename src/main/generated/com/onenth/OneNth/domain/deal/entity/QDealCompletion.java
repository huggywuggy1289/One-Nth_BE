package com.onenth.OneNth.domain.deal.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDealCompletion is a Querydsl query type for DealCompletion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDealCompletion extends EntityPathBase<DealCompletion> {

    private static final long serialVersionUID = 1839440942L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDealCompletion dealCompletion = new QDealCompletion("dealCompletion");

    public final com.onenth.OneNth.domain.common.QBaseEntity _super = new com.onenth.OneNth.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDealConfirmation dealConfirmation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAccepted = createBoolean("isAccepted");

    public final StringPath trackingNumber = createString("trackingNumber");

    public final DateTimePath<java.time.LocalDateTime> tradeDate = createDateTime("tradeDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> tradePrice = createNumber("tradePrice", Integer.class);

    public final EnumPath<com.onenth.OneNth.domain.deal.entity.enums.TradeType> tradeType = createEnum("tradeType", com.onenth.OneNth.domain.deal.entity.enums.TradeType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDealCompletion(String variable) {
        this(DealCompletion.class, forVariable(variable), INITS);
    }

    public QDealCompletion(Path<? extends DealCompletion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDealCompletion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDealCompletion(PathMetadata metadata, PathInits inits) {
        this(DealCompletion.class, metadata, inits);
    }

    public QDealCompletion(Class<? extends DealCompletion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dealConfirmation = inits.isInitialized("dealConfirmation") ? new QDealConfirmation(forProperty("dealConfirmation"), inits.get("dealConfirmation")) : null;
    }

}

