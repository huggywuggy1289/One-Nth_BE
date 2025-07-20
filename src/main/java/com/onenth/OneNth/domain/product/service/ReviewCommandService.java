package com.onenth.OneNth.domain.product.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.converter.ReviewConverter;
import com.onenth.OneNth.domain.product.dto.ReviewRequestDTO;
import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.entity.*;
import com.onenth.OneNth.domain.product.repository.*;
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
public class ReviewCommandService {

    private final MemberRepository memberRepository;
    private final SharingItemRepository sharingItemRepository;
    private final SharingReviewRepository sharingReviewRepository;
    private final SharingReviewImageRepository sharingReviewImageRepository;
    private final PurchaseReviewImageRepository purchaseReviewImageRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final PurchaseReviewRepository purchaseReviewRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Transactional
    public ReviewResponseDTO.successCreateSharingReviewDTO createsharingItemReview(
            Long memberId, ReviewRequestDTO.createReview request,
            Long targetSharingItemId, List<MultipartFile> images) {

        Member reviewer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.USER_NOT_FOUND));

        SharingItem sharingItem = sharingItemRepository.findById(targetSharingItemId)
                .orElseThrow(() -> new SharingItemHandler(ErrorStatus.SHARING_ITEM_NOT_FOUND));

        SharingReview sharingReview = ReviewConverter.toSharingReview(request,reviewer,sharingItem);
        sharingReviewRepository.save(sharingReview);

        if (images != null && !images.isEmpty()) {
            saveReviewImages(images, sharingReview);
        }
        return new ReviewResponseDTO.successCreateSharingReviewDTO(sharingReview.getId());
    }

    @Transactional
    public ReviewResponseDTO.successCreatePurchaseReviewDTO createPurchaseItemReview(
            Long memberId, ReviewRequestDTO.createReview request,
            Long targetPurchaseItemId, List<MultipartFile> images) {

        Member reviewer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.USER_NOT_FOUND));

        PurchaseItem purchaseItem = purchaseItemRepository.findById(targetPurchaseItemId)
                .orElseThrow(() -> new SharingItemHandler(ErrorStatus.PURCHASE_ITEM_NOT_FOUND));

        PurchaseReview review = ReviewConverter.toPurchaseReview(request,reviewer,purchaseItem);
        purchaseReviewRepository.save(review);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String uuid = UUID.randomUUID().toString();
                String keyName = amazonS3Manager.generateReviewKeyName(uuid);
                String imageUrl = amazonS3Manager.uploadFile(keyName, image);

                PurchaseReviewImage reviewImage = PurchaseReviewImage.builder()
                        .imageUrl(imageUrl)
                        .purchaseReview(review)
                        .build();

                purchaseReviewImageRepository.save(reviewImage);
                review.addReviewImage(reviewImage);
            }
        }
        return new ReviewResponseDTO.successCreatePurchaseReviewDTO(review.getId());
    }

    private void saveReviewImages(List<MultipartFile> images, SharingReview review) {
        for (MultipartFile image : images) {
            String uuid = UUID.randomUUID().toString();
            String keyName = amazonS3Manager.generateReviewKeyName(uuid);
            String imageUrl = amazonS3Manager.uploadFile(keyName, image);

            SharingReviewImage reviewImage = SharingReviewImage.builder()
                    .imageUrl(imageUrl)
                    .sharingReview(review)
                    .build();

            sharingReviewImageRepository.save(reviewImage);
            review.addReviewImage(reviewImage);
        }
    }
}