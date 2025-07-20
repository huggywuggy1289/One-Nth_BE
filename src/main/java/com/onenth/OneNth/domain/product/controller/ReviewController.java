package com.onenth.OneNth.domain.product.controller;

import com.onenth.OneNth.domain.product.dto.ReviewRequestDTO;
import com.onenth.OneNth.domain.product.dto.ReviewResponseDTO;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.service.reviewService.ReviewCommandService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.PurchasingItemHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.SharingItemHandler;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@Tag(name = "거래 후기(리뷰) 관련 API", description = "거래 후기 관련 API입니다.")
public class ReviewController {

    private final ReviewCommandService reviewCommandService;

    @Operation(
            summary = "거래 후기 작성 API (함께 나눠요)",
            description =
                    "함께 나눠요 거래가 완료된 후, 해당 거래에 대한 후기를 작성하는 API입니다." +
                    " 리뷰 내용, 별점과 함께 최대 3장의 이미지를 첨부할 수 있습니다."
    )
    @PostMapping(value = "/sharings/{sharingItemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReviewResponseDTO.successCreateSharingReviewDTO> postSharingReview (
            @AuthUser Long memberId,
            @PathVariable("sharingItemId") Long sharingItemId,
            @RequestPart("review") ReviewRequestDTO.createReview request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new SharingItemHandler(ErrorStatus.REVIEW_CONTENT_REQUIRED);
        }
        if (request.getRate() == null ||
                request.getRate().compareTo(BigDecimal.ZERO) < 0 ||
                request.getRate().compareTo(BigDecimal.valueOf(5.0)) > 0) {
            throw new SharingItemHandler(ErrorStatus.REVIEW_RATE_OUT_OF_RANGE);
        }
        if (images != null && images.size() > 3) {
            throw new SharingItemHandler(ErrorStatus.EXCEED_REVIEW_IMAGE_LIMIT);
        }
        ReviewResponseDTO.successCreateSharingReviewDTO result
                = reviewCommandService.createSharingItemReview(memberId,request,sharingItemId, images);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "거래 후기 작성 API (같이 사요)",
            description =
                    "같이 사요 거래가 완료된 후, 해당 거래에 대한 후기를 작성하는 API입니다." +
                            " 리뷰 내용, 별점과 함께 최대 3장의 이미지를 첨부할 수 있습니다."
    )
    @PostMapping(value = "/purchase/{purchaseItemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReviewResponseDTO.successCreatePurchaseReviewDTO> postPurchaseReview (
            @AuthUser Long memberId,
            @PathVariable("purchaseItemId") Long purchaseItemId,
            @RequestPart("review") ReviewRequestDTO.createReview request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new PurchasingItemHandler(ErrorStatus.REVIEW_CONTENT_REQUIRED);
        }
        if (request.getRate() == null ||
                request.getRate().compareTo(BigDecimal.ZERO) < 0 ||
                request.getRate().compareTo(BigDecimal.valueOf(5.0)) > 0) {
            throw new PurchasingItemHandler(ErrorStatus.REVIEW_RATE_OUT_OF_RANGE);
        }
        if (images != null && images.size() > 3) {
            throw new PurchasingItemHandler(ErrorStatus.EXCEED_REVIEW_IMAGE_LIMIT);
        }

        ReviewResponseDTO.successCreatePurchaseReviewDTO result
                = reviewCommandService.createPurchaseItemReview(memberId,request,purchaseItemId, images);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "내가 쓴 거래 후기 수정 API",
            description ="사용자가 작성한 거래 후기의 '본문 내용'과 '별점(rate)'을 수정합니다. 본인이 작성한 리뷰에 대해서만 수정 권한이 있으며," +
                    " itemType 파라미터를 통해 어떤 유형의 거래 후기인지(PURCHASE 또는 SHARE)를 구분합니다."
    )
    @PatchMapping(value = "/{reviewId}")
    public ApiResponse<String> updateReview(
            @AuthUser Long memberId,
            @PathVariable("reviewId") Long reviewId,
            @Parameter(description = "거래 유형 (PURCHASE: 같이 사요 후기, SHARE: 함께 나눠요 후기)")
            @RequestParam("itemType") ItemType itemType,
            @RequestBody ReviewRequestDTO.createReview request
    ) {
        reviewCommandService.updateReview(request, itemType, reviewId, memberId);
        return ApiResponse.onSuccess("거래후기의 본문, 별점 수정이 완료되었습니다.");
    }
}