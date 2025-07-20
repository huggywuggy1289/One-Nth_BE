package com.onenth.OneNth.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class ReviewRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class createSharingReview{
        private String content;
        private BigDecimal rate;
    }
}
