package com.onenth.OneNth.domain.product.repository;

import com.onenth.OneNth.domain.product.DTO.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.entity.QPurchaseItem;
import com.onenth.OneNth.domain.region.entity.QRegion;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

// PurchaseItemRepositoryCustom에 선언한 메서드는 여기서 구현
@Repository
@RequiredArgsConstructor
public class PurchaseItemRepositoryImpl implements PurchaseItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PurchaseItemListDTO> findByRegion(Integer regionId) {
        QPurchaseItem item = QPurchaseItem.purchaseItem;
        QRegion region = QRegion.region;

        return queryFactory
                .select(Projections.constructor(
                        PurchaseItemListDTO.class,
                        item.id,
                        item.itemCategory.stringValue(),
                        item.name,
                        item.price.stringValue(),
                        Expressions.constant("") // 썸네일 URL 추후 추가
                ))
                .from(item)
                .join(item.region, region)
                .where(item.region.id.eq(regionId.intValue()))
                .fetch();
    }

    @Override
    public List<PurchaseItemListDTO> findByCategoryAndRegions(String category, List<Long> regionIds) {
        QPurchaseItem item = QPurchaseItem.purchaseItem;

        return queryFactory
                .select(Projections.constructor(
                        PurchaseItemListDTO.class,
                        item.id,
                        item.itemCategory.stringValue(),
                        item.name,
                        item.price.stringValue(),
                        Expressions.constant("") // 썸네일 URL 추후 추가
                ))
                .from(item)
                .where(item.region.id.in(regionIds.stream().map(Long::intValue).toList()))
                .fetch();
    }
}