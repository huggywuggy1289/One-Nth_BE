package com.onenth.OneNth.domain.product.service.reviewService;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.converter.ReviewConverter;
import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.review.*;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.purchase.PurchaseReviewRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.sharing.SharingReviewRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.PurchasingItemHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.SharingItemHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryServiceImpl implements ReviewQueryService{

    private final MemberRepository memberRepository;

    private final SharingReviewRepository sharingReviewRepository;
    private final PurchaseReviewRepository purchaseReviewRepository;

    private final SharingItemRepository sharingItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    @Override
    public ReviewResponseDTO.getReviewListDTO getMemberReviews(Long memberId) {

        Member targetMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<ReviewResponseDTO.getReviewDTO> getReviewDTOList = new ArrayList<>();

        List<SharingReview> sharingReviewList = sharingReviewRepository.findByMember(targetMember);
        for (SharingReview review : sharingReviewList) {
            Long revieweeId = review.getSharingItem().getMember().getId();
            getReviewDTOList.add(toReviewDTO(review, ItemType.SHARE, revieweeId));
        }

        List<PurchaseReview> purchaseReviewList = purchaseReviewRepository.findByMember(targetMember);
        for (PurchaseReview review : purchaseReviewList) {
            Long revieweeId = review.getPurchaseItem().getMember().getId();
            getReviewDTOList.add(toReviewDTO(review, ItemType.PURCHASE, revieweeId));
        }

        getReviewDTOList.sort(Comparator.comparing(ReviewResponseDTO.getReviewDTO::getCreatedAt).reversed());
        return ReviewConverter.toGetReviewListDTO(getReviewDTOList, targetMember.getId());
    }

    @Override
    public ReviewResponseDTO.getReviewDTO getReviewDetails(Long reviewId, Long userId, ItemType itemType) {

        Review review = switch (itemType) {
            case SHARE -> sharingReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new SharingItemHandler(ErrorStatus.REVIEW_NOT_FOUND));
            case PURCHASE -> purchaseReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new PurchasingItemHandler(ErrorStatus.REVIEW_NOT_FOUND));
        };

        Long itemId = review.getItemId();

        List<String> imageList = review.getReviewImages().stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList());

        return ReviewConverter.toGetReviewDTO(review, imageList, itemType, userId, itemId);
    }

    @Override
    public ReviewResponseDTO.getReviewListDTO getUserReceivedReviews(Long userId) {

        Member targetMember = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<ReviewResponseDTO.getReviewDTO> getReviewDTOList = new ArrayList<>();

        List<SharingItem> sharingItems = sharingItemRepository.findByMember(targetMember);
        for (SharingItem item : sharingItems) {
            if (item.getSharingReviews().isEmpty()) continue;

            Long revieweeId = item.getMember().getId();
            for (SharingReview review : item.getSharingReviews()) {
                getReviewDTOList.add(toReviewDTO(review, ItemType.SHARE, revieweeId));
            }
        }

        List<PurchaseItem> purchaseItems = purchaseItemRepository.findByMember(targetMember);
        for (PurchaseItem item : purchaseItems) {
            if (item.getPurchaseReviews().isEmpty()) continue;

            Long revieweeId = item.getMember().getId();
            for (PurchaseReview review : item.getPurchaseReviews()) {
                getReviewDTOList.add(toReviewDTO(review, ItemType.PURCHASE, revieweeId));
            }
        }
        getReviewDTOList.sort(Comparator.comparing(ReviewResponseDTO.getReviewDTO::getCreatedAt).reversed());
        return ReviewConverter.toGetReviewListDTO(getReviewDTOList, targetMember.getId());
    }

    private ReviewResponseDTO.getReviewDTO toReviewDTO(Review review, ItemType itemType, Long targetUserId) {
        List<String> imageList = review.getReviewImages()
                .stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList());
        Long itemId = review.getItemId();
        return ReviewConverter.toGetReviewDTO(review, imageList, itemType, targetUserId, itemId);
    }
}