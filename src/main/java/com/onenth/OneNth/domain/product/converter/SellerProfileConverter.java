package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.dto.SellerProfileResponseDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.review.PurchaseReview;
import com.onenth.OneNth.domain.product.entity.review.PurchaseReviewImage;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.region.entity.Region;

import java.util.List;

public class SellerProfileConverter {

    public static SellerProfileResponseDTO toResponse(Member member,
                                                      Region region,
                                                      List<PurchaseItem> items,
                                                      List<PurchaseReview> reviews) {

        List<SellerProfileResponseDTO.PurchaseItemSummaryDTO> itemDTOs = items.stream()
                .map(item -> SellerProfileResponseDTO.PurchaseItemSummaryDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .itemCategory(item.getItemCategory())
                        .purchaseMethod(item.getPurchaseMethod())
                        .thumbnailUrl(item.getItemImages().isEmpty() ? null : item.getItemImages().get(0).getUrl())
                        .build())
                .toList();

        List<ReviewResponseDTO.getReviewDTO> reviewDTOs = reviews.stream()
                .limit(3)
                .map(r -> ReviewResponseDTO.getReviewDTO.builder()
                        .reviewId(r.getId())
                        .reviewerId(r.getMember().getId())
                        .itemId(r.getPurchaseItem().getId())
                        .itemType(ItemType.PURCHASE)
                        .createdAt(r.getCreatedAt())
                        .content(r.getContent())
                        .rate(r.getRate())
                        .reviewImageList(
                                r.getReviewImages().stream()
                                        .map(img -> ((PurchaseReviewImage) img).getImageUrl())
                                        .toList()
                        )
                        .build())
                .toList();

        return SellerProfileResponseDTO.builder()
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .isVerified(member.isVerified())
                .mainRegionName(region.getRegionName())
                .totalSalesCount(items.size())
                .totalReviewCount(reviews.size())
                .averageRating(reviews.stream()
                        .mapToDouble(r -> r.getRate().doubleValue())
                        .average().orElse(0.0))
                .items(itemDTOs)
                .recentReviews(reviewDTOs)
                .build();
    }
}

