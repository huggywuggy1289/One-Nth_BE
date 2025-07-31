package com.onenth.OneNth.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class ReviewRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class createReview{
        private String content;
        private BigDecimal rate;
    }

    @Getter
    @NoArgsConstructor
    public static class DeleteReviewImages {
        private List<Long> imageIds;
    }
}