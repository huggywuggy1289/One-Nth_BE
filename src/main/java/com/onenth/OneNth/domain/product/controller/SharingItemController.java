package com.onenth.OneNth.domain.product.controller;

import com.onenth.OneNth.domain.product.converter.SharingItemConverter;
import com.onenth.OneNth.domain.product.dto.SharingItemListDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemRequestDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.service.SharingItemService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sharing-items")
@RequiredArgsConstructor
@Tag(name = "함께나눠요 관련 API", description = "함께나눠요 상품 등록, 상품 조회 관련 API 지원")
public class SharingItemController {

    private final SharingItemService sharingItemService;

    // 상품 등록
    @Operation(
            summary = "함께 나눠요 상품 등록 API",
            description = "함께 나눠요 상품을 등록합니다. \n\n" +
                    "- 소비기한은 식품 카테고리(FOOD)일 때만 필수입니다.\n" +
                    "- 이미지는 최소 1장 이상, 최대 3장까지 첨부 가능합니다.\n" +
                    "- 태그는 반드시 #으로 시작해야 합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SharingItemResponseDTO.RegisterResponse> registerSharingItem(
            @RequestPart("data") @Valid SharingItemRequestDTO dto,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @AuthUser Long userId
    ) {
        Long savedId = sharingItemService.registerItem(dto, imageFiles, userId);
        return ApiResponse.onSuccess(SharingItemConverter.toRegisterResponse(savedId));
    }

    // 상품 검색
    @Operation(
            summary = "함께나눠요 상품 검색",
            description = """
                    키워드로 상품을 검색합니다.
                    
                    - `#태그명` : 설정한 3개 지역 내 태그로 검색
                    - `카테고리명` : 설정한 3개 지역 내 카테고리로 검색
                    - `지역명` : 설정과 무관하게 특정 지역명으로 검색
                    """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<SharingItemListDTO>>> searchSharingItems(
            @RequestParam String keyword,
            @AuthUser Long userId
    ) {
        List<SharingItemListDTO> result = sharingItemService.searchItems(keyword, userId);
        log.info("keyword: [{}]", keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

    // 상품검색(상품명)
    @Operation(
            summary = "상품명 기반 지역 선택 검색",
            description = """
                    - 설정한 3개 지역 내 상품명으로 검색
                    """
    )

    @GetMapping("/title")
    public ApiResponse<List<SharingItemListDTO>> searchByTitleWithRegionFilter(
            @RequestParam String keyword,
            @RequestParam(required = false) List<Integer> regionIds,
            @AuthUser Long userId
    ) {
        List<SharingItemListDTO> results = sharingItemService.searchByTitleInUserRegions(keyword, userId);
        return ApiResponse.onSuccess(results);
    }

    @Operation(
            summary = "함께 나눠요 단일 상품 조회",
            description = "ID를 기준으로 함께 나눠요 상품의 상세 정보를 조회합니다."
    )
    @GetMapping("/{sharingItemId}")
    public ApiResponse<SharingItemResponseDTO.GetResponse> getSharingItemDetail(
            @PathVariable Long sharingItemId,
            @AuthUser Long userId
    ) {
        SharingItemResponseDTO.GetResponse detail = sharingItemService.getItemDetail(sharingItemId, userId);
        return ApiResponse.onSuccess(detail);
    }

    @Operation(
            summary = "함께 나눠요 상품 상태 전환 API",
            description = """
                    특정 함께 나눠요 상품의 상태를 전환합니다. (판매 중 <-> 판매 완료)
                    - URL 경로에 포함된 `groupPurchaseId` 위치에 상태를 전환하고자 하는 함께 나눠요 상품의 Id를 넣어 요청합니다.
                    - 쿼리 파라미터 'status'에 후기에 전환하고자 하는 상태를 명시합니다. (DEFAULT: 판매중, COMPLETED: 판매완료)
                    """
    )
    @PatchMapping(value = "/{sharingItemId}/status")
    public ApiResponse<String> changeItemStatus(
            @AuthUser Long memberId,
            @PathVariable("sharingItemId") Long purchaseItemId,
            @Parameter(description = "상품 상태 (DEFAULT: 판매중, COMPLETED: 판매완료)")
            @RequestParam("status") Status status) {
        sharingItemService.changeItemStatus(purchaseItemId, memberId, status);
        return ApiResponse.onSuccess("상품 상태 전환이 완료되었습니다.");
    }
}