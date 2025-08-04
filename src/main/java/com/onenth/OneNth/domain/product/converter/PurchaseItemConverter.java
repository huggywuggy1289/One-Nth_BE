package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.product.dto.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.dto.PurchaseItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;

import java.util.List;
import java.util.Set;

public class PurchaseItemConverter {

    public static PurchaseItemResponseDTO.registerPurchaseItemResponseDTO toRegisterPurchaseItemResponseDTO(Long purchaseItemId){
        return PurchaseItemResponseDTO.registerPurchaseItemResponseDTO.builder()
                .purchaseItem(purchaseItemId)
                .build();
    }

    public static List<PurchaseItemListDTO> toPurchaseItemListDTOs(List<PurchaseItem> items, Set<Long> bookmarkedIds) {
        return items.stream()
                .map(item -> PurchaseItemListDTO.fromEntity(item, bookmarkedIds.contains(item.getId())))
                .toList();
    }
}
