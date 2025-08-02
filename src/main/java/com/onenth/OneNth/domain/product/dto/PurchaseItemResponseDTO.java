package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class PurchaseItemResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class registerPurchaseItemResponseDTO {
        private Long purchaseItem;
    }

    // 단일 상품
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetPurchaseItemResponseDTO {
        private String title;
        private List<String> imageUrls;
        private String purchaseUrl;
        private LocalDate expirationDate;
        private String writerNickname;
        private String writerProfileImageUrl;
        private boolean writerVerified;
        private ItemCategory itemCategory;
        private PurchaseMethod purchaseMethod;
        private Integer price;

        private Double latitude;
        private Double longitude;
    }
}