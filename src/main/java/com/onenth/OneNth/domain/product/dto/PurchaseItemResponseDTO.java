package com.onenth.OneNth.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PurchaseItemResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class registerPurchaseItemResponseDTO {
        private Long purchaseItem;
    }
}