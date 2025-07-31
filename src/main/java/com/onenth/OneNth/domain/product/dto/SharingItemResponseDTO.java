package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class SharingItemResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterResponse {
        private Long sharingItem;
    }

    //단일 상세 조회용
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetResponse {
        private Long id;
        private String title;
        private Integer quantity;
        private Integer price;
        private ItemCategory itemCategory;
        private LocalDate expirationDate;
        private Boolean isAvailable;
        private PurchaseMethod purchaseMethod;
        private String regionName;
        private List<String> imageUrls;
        private List<String> tags;
        private String writerNickname;
    }
}

