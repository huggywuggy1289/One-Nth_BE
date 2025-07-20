package com.onenth.OneNth.domain.product.service.reviewService;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.converter.ReviewConverter;
import com.onenth.OneNth.domain.product.dto.ReviewRequestDTO;
import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.entity.*;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.purchase.PurchaseReviewImageRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.purchase.PurchaseReviewRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.sharing.SharingReviewImageRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.sharing.SharingReviewRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.SharingItemHandler;
import com.onenth.OneNth.global.aws.s3.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final MemberRepository memberRepository;
    private final SharingItemRepository sharingItemRepository;
    private final SharingReviewRepository sharingReviewRepository;
    private final SharingReviewImageRepository sharingReviewImageRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final PurchaseReviewRepository purchaseReviewRepository;
    private final PurchaseReviewImageRepository purchaseReviewImageRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Transactional
    @Override
    public ReviewResponseDTO.successCreateSharingReviewDTO createSharingItemReview(
            Long memberId, ReviewRequestDTO.createReview request,
            Long targetSharingItemId, List<MultipartFile> images) {

        Member reviewer = findMemberById(memberId);
        SharingItem item = sharingItemRepository.findById(targetSharingItemId)
                .orElseThrow(() -> new SharingItemHandler(ErrorStatus.SHARING_ITEM_NOT_FOUND));

        SharingReview review = ReviewConverter.toSharingReview(request, reviewer, item);
        sharingReviewRepository.save(review);

        saveReviewImages(images, review, ReviewType.SHARING);

        return new ReviewResponseDTO.successCreateSharingReviewDTO(review.getId());
    }

    @Transactional
    @Override
    public ReviewResponseDTO.successCreatePurchaseReviewDTO createPurchaseItemReview(
            Long memberId, ReviewRequestDTO.createReview request,
            Long targetPurchaseItemId, List<MultipartFile> images) {

        Member reviewer = findMemberById(memberId);
        PurchaseItem item = purchaseItemRepository.findById(targetPurchaseItemId)
                .orElseThrow(() -> new SharingItemHandler(ErrorStatus.PURCHASE_ITEM_NOT_FOUND));

        PurchaseReview review = ReviewConverter.toPurchaseReview(request, reviewer, item);
        purchaseReviewRepository.save(review);

        saveReviewImages(images, review, ReviewType.PURCHASE);

        return new ReviewResponseDTO.successCreatePurchaseReviewDTO(review.getId());
    }

    private void saveReviewImages(List<MultipartFile> images, Object review, ReviewType type) {
        if (images == null || images.isEmpty()) return;

        for (MultipartFile image : images) {
            String uuid = UUID.randomUUID().toString();
            String keyName = amazonS3Manager.generateReviewKeyName(uuid);
            String imageUrl = amazonS3Manager.uploadFile(keyName, image);

            if (type == ReviewType.SHARING && review instanceof SharingReview sharingReview) {
                SharingReviewImage reviewImage = SharingReviewImage.builder()
                        .imageUrl(imageUrl)
                        .sharingReview(sharingReview)
                        .build();

                sharingReviewImageRepository.save(reviewImage);
                sharingReview.addReviewImage(reviewImage);
            }

            if (type == ReviewType.PURCHASE && review instanceof PurchaseReview purchaseReview) {
                PurchaseReviewImage reviewImage = PurchaseReviewImage.builder()
                        .imageUrl(imageUrl)
                        .purchaseReview(purchaseReview)
                        .build();

                purchaseReviewImageRepository.save(reviewImage);
                purchaseReview.addReviewImage(reviewImage);
            }
        }
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.USER_NOT_FOUND));
    }

    private enum ReviewType {
        SHARING, PURCHASE
    }
}