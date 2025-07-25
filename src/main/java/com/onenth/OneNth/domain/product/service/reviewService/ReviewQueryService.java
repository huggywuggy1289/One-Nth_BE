package com.onenth.OneNth.domain.product.service.reviewService;

import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;

public interface ReviewQueryService {
    ReviewResponseDTO.getReviewListDTO getMemberReviews(Long memberId);
    ReviewResponseDTO.getReviewDTO getReviewDetails(Long reviewId, Long userId, ItemType itemType);
}
