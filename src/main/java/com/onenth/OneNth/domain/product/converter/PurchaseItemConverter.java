package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.product.dto.PurchaseItemResponseDTO;

public class PurchaseItemConverter {

    public static PurchaseItemResponseDTO.registerPurchaseItemResponseDTO toRegisterPurchaseItemResponseDTO(Long purchaseItemId){
        return PurchaseItemResponseDTO.registerPurchaseItemResponseDTO.builder()
                .purchaseItem(purchaseItemId)
                .build();
    }
}
