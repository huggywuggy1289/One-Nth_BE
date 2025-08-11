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
    public static class getProducPreviewtDTO{
        private Long itemId;
        private ItemType itemType;
        private String itemName;
        private String itemImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetDealConfirmationDTO{
        private Long dealConfirmationId;
        private Long itemId;
        private ItemType itemType;
        private String itemName;
        private String itemImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyDealHistoryDTO{
        private Integer totalReviewCount;
        private Double totalReviewRating;
        private Integer savedAmount;
        private DealHistoryDetailDTO totalDealHistory;
        private DealHistoryDetailDTO purchaseDealHistory;
        private DealHistoryDetailDTO shareDealHistory;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealHistoryDetailDTO{
        private Integer totalDealCount;
        private Integer totalDealAmount ;
    }
}