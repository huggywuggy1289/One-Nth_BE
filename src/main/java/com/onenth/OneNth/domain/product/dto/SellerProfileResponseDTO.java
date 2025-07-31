package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerProfileResponseDTO {

    private String nickname;
    private boolean isVerified;
    private int totalSalesCount;
    private int totalReviewCount;
    private double averageRating;
    private String profileImageUrl;
    private String mainRegionName;

    private List<PurchaseItemSummaryDTO> items;
    private List<ReviewResponseDTO.getReviewDTO> recentReviews;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseItemSummaryDTO {
        private Long id;
        private String name;
        private int price;
        private ItemCategory itemCategory;
        private PurchaseMethod purchaseMethod;
        private String thumbnailUrl;
    }
}
