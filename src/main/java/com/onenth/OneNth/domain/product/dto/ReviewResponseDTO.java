package com.onenth.OneNth.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewResponseDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class successCreateSharingReviewDTO {
        private Long sharingReviewId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class successCreatePurchaseReviewDTO {
        private Long puchaseReviewId;
    }
}
