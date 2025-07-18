package com.onenth.OneNth.domain.product.repository;

import com.onenth.OneNth.domain.product.entity.PurchaseItem;

import java.util.List;

public interface PurchaseItemRepositoryCustom {
    // 태그로 검색하면 내가 설정한 지역에서 그 태그가 포함된 상품을 조회
    List<PurchaseItem> findByRegionsAndTag(List<Integer> regionIds, String tag);

    // 카테고리로 검색하면 내가 설정한 지역에서, 그 카테고리가 포함된 상품만 조회
    List<PurchaseItem> findByRegionsAndCategory(List<Integer> regionIds, String category);
    // 지역명으로 검색하면 설정한 지역 상관없이 모든 지역의 상품조회
    List<PurchaseItem> findByRegionName(String regionName);
}
