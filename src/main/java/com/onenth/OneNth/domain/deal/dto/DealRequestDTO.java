package com.onenth.OneNth.domain.deal.dto;

import com.onenth.OneNth.domain.deal.entity.enums.TradeType;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class DealRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DealConfirmationRequestDTO {
        private Long itemId;
        private ItemType itemType;
        private LocalDate dealDate;
        private TradeType tradeType;
        private Integer purchasePrice;
        private Integer originalPrice;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DealCompletionRequestDTO {
        private Long dealConfirmationId;
        private LocalDate dealDate;
        private Integer tradePrice;
        private Integer tradeCount;
        private TradeType tradeType;
    }
}
