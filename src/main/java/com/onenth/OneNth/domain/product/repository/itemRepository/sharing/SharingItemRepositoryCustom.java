package com.onenth.OneNth.domain.product.repository.itemRepository.sharing;

import com.onenth.OneNth.domain.product.entity.SharingItem;

import java.util.List;

public interface SharingItemRepositoryCustom {

    // 태그(지역필터링) 검색
    List<SharingItem> findByRegionAndTag(List<Integer> regionIds, String tag);
    // 카테고리(지역필터링) 검색
    List<SharingItem> findByRegionAndCategory(List<Integer> regionIds, String category);
    // 지역명(지역 필터링x) 검색
    List<SharingItem> findByRegionName(String regionName);
}
