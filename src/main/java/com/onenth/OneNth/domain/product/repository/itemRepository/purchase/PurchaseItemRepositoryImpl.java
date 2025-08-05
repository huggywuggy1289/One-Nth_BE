package com.onenth.OneNth.domain.product.repository.itemRepository.purchase;

import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.QPurchaseItem;
import com.onenth.OneNth.domain.product.entity.QTag;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.region.entity.QRegion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;


import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PurchaseItemRepositoryImpl implements PurchaseItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PurchaseItem> findByRegionsAndTag(List<Integer> regionIds, String tag) {
        QPurchaseItem item = QPurchaseItem.purchaseItem;
        QTag qTag = QTag.tag;

        return queryFactory
                .selectFrom(item)
                .join(item.tags, qTag)
                .where(
                        item.region.id.in(regionIds)
                                .and(qTag.name.eq(tag))
                                .and(item.status.in(Status.DEFAULT, Status.IN_PROGRESS))
                )
                .fetch();
    }

    @Override
    public List<PurchaseItem> findByRegionsAndCategory(List<Integer> regionIds, String category) {
        QPurchaseItem item = QPurchaseItem.purchaseItem;
        return queryFactory.selectFrom(item)
                .where(
                        item.region.id.in(regionIds)
                                .and(item.itemCategory.eq(ItemCategory.valueOf(category.toUpperCase())))
                                .and(item.status.in(Status.DEFAULT, Status.IN_PROGRESS))

                )
                .fetch();
    }

    @Override
    public List<PurchaseItem> findByRegionsName(String regionName) {
        QPurchaseItem item = QPurchaseItem.purchaseItem;
        QRegion region = QRegion.region;

        String cleanKeyword = regionName.trim();

        return queryFactory
                .selectFrom(item)
                .join(item.region, region)
                .where(
                        region.regionName.like("%" + cleanKeyword + "%")
                                .and(item.status.in(Status.DEFAULT, Status.IN_PROGRESS))
                )
                .fetch();
    }

    @Override
    public List<PurchaseItem> searchByTitleAndRegion(String keyword, List<Integer> regionIds) {
        log.info("QueryDSL searchByTitleAndRegion 실행");
        QPurchaseItem item = QPurchaseItem.purchaseItem;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(item.name.containsIgnoreCase(keyword));
        builder.and(item.status.in(Status.DEFAULT, Status.IN_PROGRESS));

        if (regionIds != null && !regionIds.isEmpty()) {
            builder.and(item.region.id.in(regionIds));
        }
        log.info("regionIds = {}", regionIds);
        log.info("필터링된 쿼리 조건: name LIKE '%{}%', region.id in {}", keyword, regionIds);


        return queryFactory
                .selectFrom(item)
                .join(item.region).fetchJoin()
                .where(builder)
                .fetch();
    }
}