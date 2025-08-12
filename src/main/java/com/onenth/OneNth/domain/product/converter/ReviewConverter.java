package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.dto.ReviewRequestDTO;
import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.review.PurchaseReview;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.review.Review;
import com.onenth.OneNth.domain.product.entity.review.ReviewImage;
import com.onenth.OneNth.domain.product.entity.review.SharingReview;

import java.util.List;

public class ReviewConverter {

    public static SharingReview toSharingReview(ReviewRequestDTO.createReview request, Member member, SharingItem sharingItem) {
        return SharingReview.builder()
                .rate(request.getRate())
                .content(request.getContent())
                .member(member)
                .sharingItem(sharingItem)
                .build();
    }

    public static PurchaseReview toPurchaseReview(ReviewRequestDTO.createReview request, Member member, PurchaseItem purchaseItem) {
        return PurchaseReview.builder()
                .rate(request.getRate())
                .content(request.getContent())
                .member(member)
                .purchaseItem(purchaseItem)
                .build();
    }

    public static ReviewResponseDTO.getReviewDTO toGetReviewDTO(
            Review review, List<String> imageUrl, ItemType itemType, Long targetUserId, Long itemId) {
        return ReviewResponseDTO.getReviewDTO.builder()
                .reviewId(review.getId())
                .itemType(itemType)
                .itemId(itemId)
                .reviewerId(review.getMember().getId())
                .reviewerNickName(review.getMember().getNickname())
                .reviewerProfileImageUrl(review.getMember().getProfileImageUrl())
                .reviewTargetId(targetUserId)
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .rate(review.getRate())
                .reviewImageList(imageUrl)
                .build();
    }

    public static ReviewResponseDTO.getReviewListDTO toGetReviewListDTO(
            List<ReviewResponseDTO.getReviewDTO> getReviewDTOList
            , Member member) {
        return ReviewResponseDTO.getReviewListDTO.builder()
                .memberId(member.getId())
                .memberNickName(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .reviewList(getReviewDTOList)
                .build();
    }

    public static ReviewResponseDTO.getImageDetail toGetImageDetail(ReviewImage reviewImage) {
        return ReviewResponseDTO.getImageDetail.builder()
                .reviewImageId(reviewImage.getId())
                .imageUrl(reviewImage.getImageUrl())
                .build();
    }

    public static ReviewResponseDTO.getMyReviewDTO toGetMyReviewDTO(
            Review review, List<ReviewResponseDTO.getImageDetail> image, ItemType itemType, Long targetUserId, Long itemId
    ){
        return ReviewResponseDTO.getMyReviewDTO.builder()
                .reviewId(review.getId())
                .itemType(itemType)
                .itemId(itemId)
                .reviewerId(review.getMember().getId())
                .reviewTargetId(targetUserId)
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .rate(review.getRate())
                .reviewImageList(image)
                .build();
    }
}