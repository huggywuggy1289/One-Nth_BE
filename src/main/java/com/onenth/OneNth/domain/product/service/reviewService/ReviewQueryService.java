package com.onenth.OneNth.domain.product.service.reviewService;

import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;

public interface ReviewQueryService {
    ReviewResponseDTO.getReviewListDTO getMemberReviews(Long memberId);
}
