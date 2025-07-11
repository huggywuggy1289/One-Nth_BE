package com.onenth.OneNth.domain.product.repository;

import com.onenth.OneNth.domain.product.DTO.PurchaseItemListDTO;

import java.util.List;

// 수동 쿼리메서드 정의(구현은 Impl)
public interface PurchaseItemRepositoryCustom {
    List<PurchaseItemListDTO> findByRegion(Integer regionId);
    List<PurchaseItemListDTO> findByCategoryAndRegions(String category, List<Long> regionIds);
}
