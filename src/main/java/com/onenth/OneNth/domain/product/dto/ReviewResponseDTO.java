package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.review.ReviewImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class getReviewDTO{
        private Long reviewId;
        private ItemType itemType;
        private Long itemId;
        private LocalDateTime createdAt;
        private Long reviewerId;
        private Long reviewTargetId;
        private String content;
        private BigDecimal rate;
        private List<String> reviewImageList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class getReviewListDTO{
        private Long memberId;
        private List<getReviewDTO> reviewList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class getMyReviewDTO{
        private Long reviewId;
        private ItemType itemType;
        private Long itemId;
        private LocalDateTime createdAt;
        private Long reviewerId;
        private Long reviewTargetId;
        private String content;
        private BigDecimal rate;
        private List<getImageDetail> reviewImageList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class getImageDetail{ 
        private Long reviewImageId;
        private String imageUrl;
    }
}
