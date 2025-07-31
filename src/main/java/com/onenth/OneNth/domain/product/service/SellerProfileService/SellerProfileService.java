package com.onenth.OneNth.domain.product.service.SellerProfileService;


import com.amazonaws.services.kms.model.NotFoundException;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.dto.SellerProfileResponseDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.review.PurchaseReview;
import com.onenth.OneNth.domain.product.entity.review.PurchaseReviewImage;
import com.onenth.OneNth.domain.product.entity.review.SharingReview;
import com.onenth.OneNth.domain.product.entity.review.SharingReviewImage;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.purchase.PurchaseReviewRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.sharing.SharingReviewRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SellerProfileService {

    private final MemberRepository memberRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final PurchaseReviewRepository purchaseReviewRepository;
    private final SharingItemRepository sharingItemRepository;
    private final SharingReviewRepository sharingReviewRepository;
    public SellerProfileResponseDTO getPurchaseSellerProfile(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        MemberRegion memberRegion = memberRegionRepository.findByMemberId(member.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("대표 지역이 없습니다."));

        Region region = memberRegion.getRegion();

        List<PurchaseItem> items = purchaseItemRepository.findByMember(member);

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

        List<PurchaseReview> reviews = purchaseReviewRepository.findByMember(member);
        int reviewCount = reviews.size();
        double avgRating = reviews.stream()
                .mapToDouble(r -> r.getRate().doubleValue())
                .average().orElse(0.0);

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
                .totalReviewCount(reviewCount)
                .averageRating(avgRating)
                .items(itemDTOs)
                .recentReviews(reviewDTOs)
                .build();
    }

    public SellerProfileResponseDTO getSharingSellerProfile(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("사용자 없음"));

        Region region = memberRegionRepository.findByMemberId(memberId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("대표 지역 없음"))
                .getRegion();

        List<SharingItem> items = sharingItemRepository.findByMember(member);

        List<SellerProfileResponseDTO.PurchaseItemSummaryDTO> itemDTOs = items.stream()
                .map(item -> SellerProfileResponseDTO.PurchaseItemSummaryDTO.builder()
                        .id(item.getId())
                        .name(item.getTitle())
                        .price(item.getPrice())
                        .itemCategory(item.getItemCategory())
                        .purchaseMethod(item.getPurchaseMethod())
                        .thumbnailUrl(item.getItemImages().isEmpty() ? null : item.getItemImages().get(0).getUrl())
                        .build())
                .toList();

        List<SharingReview> reviews = sharingReviewRepository.findByMember(member);
        int reviewCount = reviews.size();
        double avgRating = reviews.stream()
                .mapToDouble(r -> r.getRate().doubleValue())
                .average().orElse(0.0);

        List<ReviewResponseDTO.getReviewDTO> reviewDTOs = reviews.stream()
                .limit(3)
                .map(r -> ReviewResponseDTO.getReviewDTO.builder()
                        .reviewId(r.getId())
                        .reviewerId(r.getMember().getId())
                        .itemId(r.getSharingItem().getId())
                        .itemType(ItemType.SHARE)
                        .createdAt(r.getCreatedAt())
                        .content(r.getContent())
                        .rate(r.getRate())
                        .reviewImageList(
                                r.getReviewImages().stream()
                                        .map(img -> ((SharingReviewImage) img).getImageUrl())
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
                .totalReviewCount(reviewCount)
                .averageRating(avgRating)
                .items(itemDTOs)
                .recentReviews(reviewDTOs)
                .build();
    }
}