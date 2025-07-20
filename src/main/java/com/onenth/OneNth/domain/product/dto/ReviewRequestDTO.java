package com.onenth.OneNth.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class ReviewRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class createReview{
        private String content;
        private BigDecimal rate;
    }
}
