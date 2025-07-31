package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.product.dto.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.dto.PurchaseItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;

import java.util.List;

public class PurchaseItemConverter {

    public static PurchaseItemResponseDTO.registerPurchaseItemResponseDTO toRegisterPurchaseItemResponseDTO(Long purchaseItemId){
        return PurchaseItemResponseDTO.registerPurchaseItemResponseDTO.builder()
                .purchaseItem(purchaseItemId)
                .build();
    }

    public static List<PurchaseItemListDTO> toPurchaseItemListDTOs(List<PurchaseItem> items) {
        return items.stream()
                .map(PurchaseItemListDTO::fromEntity)
                .toList();
    }
}
