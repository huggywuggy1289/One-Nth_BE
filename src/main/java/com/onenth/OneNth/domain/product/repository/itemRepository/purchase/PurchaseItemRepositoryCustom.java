package com.onenth.OneNth.domain.product.repository.itemRepository.purchase;

import com.onenth.OneNth.domain.product.entity.PurchaseItem;

import java.util.List;

public interface PurchaseItemRepositoryCustom {
    // 태그
    List<PurchaseItem> findByRegionsAndTag(List<Integer> regionIds, String tag);

    // 카테고리
    List<PurchaseItem> findByRegionsAndCategory(List<Integer> regionIds, String category);
    // 지역명
    List<PurchaseItem> findByRegionsName(String regionName);

    // 상품명 + 지역 필터 검색
    List<PurchaseItem> searchByTitleAndRegion(String keyword, List<Integer> regionIds);
}
