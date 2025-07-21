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
@Tag(name = "거래 후기(리뷰) 관련 API", description = "거래 후기 작성, 수정 관련 API 지원")
public class ReviewController {

    private final ReviewCommandService reviewCommandService;

    @Operation(
            summary = "거래 후기 작성 API (함께 나눠요)",
            description = """
    '함께 나눠요' 거래가 완료된 후, 거래에 대한 후기를 작성하는 API입니다.
    - URL 경로에 포함된 `sharingItemId` 위치에 후기를 남길 물품의 ID를 넣어 요청합니다.
    - 'review' 필드에 후기 내용과 별점 정보를 JSON 문자열로 포함해야 합니다.
    - 'images' 필드에는 최대 3장까지 후기 이미지를 선택적으로 첨부할 수 있습니다.
    - 별점은 0.5 이상 5.0 이하의 숫자여야 합니다.
    """
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
            description = """
    '같이 사요' 거래가 완료된 후, 거래에 대한 후기를 작성하는 API입니다.
    - URL 경로에 포함된 `purchaseItemId` 위치에 후기를 남길 물품의 ID를 넣어 요청합니다.
    - 'review' 필드에 후기 내용과 별점 정보를 JSON 문자열로 포함해야 합니다.
    - 'images' 필드에는 최대 3장까지 후기 이미지를 선택적으로 첨부할 수 있습니다.
    - 별점은 0.5 이상 5.0 이하의 숫자여야 합니다.
    """
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
            summary = "거래 후기 수정 - 본문, 별점 수정 API",
            description = """
    작성한 거래 후기의 내용과 별점을 수정하는 API입니다. 거래 후기 수정에서 활용됩니다.
    - URL 경로에 포함된 `reviewId` 위치에 수정할 후기의 ID를 넣어 요청합니다.
    - 쿼리 파라미터 'itemType'에 후기의 '거래 유형'을 명시합니다. ('PURCHASE' (같이 사요), 'SHARE' (함께 나눠요))
    - 요청 본문에 수정할 후기 내용과 별점을 JSON 형식으로 포함합니다.
    - 본인만 수정 권한이 있습니다.
    """
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

    @Operation(
            summary = "거래 후기 수정 - 이미지 선택 삭제 API",
            description = """
    작성한 거래 후기에서 삭제를 원하는 이미지들만 삭제하는 API입니다. 거래 후기 수정에서 활용됩니다.
    - URL 경로에 포함된 `reviewId` 위치에 수정할 후기의 ID를 넣어 요청합니다.
    - 쿼리 파라미터 'itemType'에 후기에 '거래 유형'을 명시합니다. (('PURCHASE' (같이 사요), 'SHARE' (함께 나눠요))
    - 요청 본문에 삭제할 이미지의 ID 리스트를 JSON 배열로 포함합니다.
    - 본인만 삭제 권한이 있습니다.
    """
    )
    @DeleteMapping("/{reviewId}/images")
    public ApiResponse<String> deleteSelectedReviewImages(
            @AuthUser Long memberId,
            @PathVariable("reviewId") Long reviewId,
            @Parameter(description = "거래 유형 (PURCHASE: 같이 사요 후기, SHARE: 함께 나눠요 후기)")
            @RequestParam("itemType") ItemType itemType,
            @RequestBody ReviewRequestDTO.DeleteReviewImages deleteReviewImages
    ) {
        reviewCommandService.deleteReviewImage(deleteReviewImages, itemType, reviewId, memberId);
        return ApiResponse.onSuccess("선택한 이미지들이 삭제되었습니다.");
    }
}