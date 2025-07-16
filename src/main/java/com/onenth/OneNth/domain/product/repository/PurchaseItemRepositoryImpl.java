package com.onenth.OneNth.domain.product.repository;

import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.QPurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

// PurchaseItemRepositoryCustom에 선언한 메서드는 여기서 구현
@Repository
@RequiredArgsConstructor
public class PurchaseItemRepositoryImpl implements PurchaseItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // PurchaseItemRepositoryCustom의 findByRegionAndTag 주석해제시 같이 해제
//    @Override
//    public List<PurchaseItem> findByRegionAndTag(Integer regionId, String tag) {
//        throw new UnsupportedOperationException("태그 검색 기능은 아직 미구현 상태입니다.");
//    }

    @Override
    public List<PurchaseItem> findByRegionAndCategory(Integer regionId, String category) {
        QPurchaseItem item = QPurchaseItem.purchaseItem;
        return queryFactory.selectFrom(item)
                .where(item.region.id.eq(regionId)
                        .and(item.itemCategory.eq(ItemCategory.valueOf(category.toUpperCase()))))
                .fetch();
    }

    @Override
    public List<PurchaseItem> findByRegionName(String regionName) {
        QPurchaseItem item = QPurchaseItem.purchaseItem;
        return queryFactory.selectFrom(item)
                .where(item.region.regionName.eq(regionName))
                .fetch();
    }


}