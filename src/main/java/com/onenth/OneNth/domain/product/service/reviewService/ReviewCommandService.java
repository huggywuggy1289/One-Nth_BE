package com.onenth.OneNth.domain.product.service.reviewService;

import com.onenth.OneNth.domain.product.dto.ReviewRequestDTO;
import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewCommandService {

    ReviewResponseDTO.successCreateSharingReviewDTO createSharingItemReview(
            Long memberId, ReviewRequestDTO.createReview request,
            Long targetSharingItemId, List<MultipartFile> images);

    ReviewResponseDTO.successCreatePurchaseReviewDTO createPurchaseItemReview(
            Long memberId, ReviewRequestDTO.createReview request,
            Long targetPurchaseItemId, List<MultipartFile> images);

    void updateReview(ReviewRequestDTO.createReview request, ItemType itemType, Long reviewId, Long memberId);
    void deleteReviewImage(ReviewRequestDTO.DeleteReviewImages request, ItemType itemType, Long reviewId, Long memberId);
    void uploadNewReviewImage(List<MultipartFile> images, ItemType itemType, Long reviewId, Long memberId);
}