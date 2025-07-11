package com.onenth.OneNth.domain.product.repository;

import com.onenth.OneNth.domain.product.DTO.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;

import java.util.List;

// 수동 쿼리메서드 정의(구현은 Impl)
public interface PurchaseItemRepositoryCustom {
    // 태그로 검색하면 내가 설정한 지역에서 그 태그가 포함된 상품을 조회(현재 미구현되어있음)
//    List<PurchaseItem> findByRegionAndTag(Integer regionId, String tag);

    // 카테고리로 검색하면 내가 설정한 지역에서, 그 카테고리가 포함된 상품만 조회
    List<PurchaseItem> findByRegionAndCategory(Integer regionId, String category);
    // 지역명으로 검색하면 설정한 지역 상관없이 모든 지역의 상품조회
    List<PurchaseItem> findByRegionName(String regionName);
}
