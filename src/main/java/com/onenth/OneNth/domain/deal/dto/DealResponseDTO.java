package com.onenth.OneNth.domain.deal.dto;


import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DealResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getAvailableProductDTO{
        private Long itemId;
        private ItemType itemType;
        private String itemName;
        private String itemImageUrl;
    }
}