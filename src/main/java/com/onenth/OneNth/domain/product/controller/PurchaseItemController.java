package com.onenth.OneNth.domain.product.controller;

import com.onenth.OneNth.domain.product.converter.PurchaseItemConverter;
import com.onenth.OneNth.domain.product.dto.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.dto.PurchaseItemRequestDTO;
import com.onenth.OneNth.domain.product.dto.PurchaseItemResponseDTO;
import com.onenth.OneNth.domain.product.service.PurchaseItemService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/group-purchases")
@RequiredArgsConstructor
@Tag(name = "같이사요 관련 API", description = "같이사요 상품 등록, 상품 조회 관련 API 지원")
public class PurchaseItemController {

    private final PurchaseItemService purchaseItemService;

    // 상품등록
    @Operation(
            summary = "같이 사요 상품 등록 API",
            description = """
        같이 사요 상품을 등록합니다.

        - 소비기한은 식품 카테고리(FOOD)일 때만 필수입니다.
        - 이미지는 최소 1장 이상, 최대 3장까지 첨부 가능합니다.
        - 태그는 반드시 #으로 시작해야 합니다.
        """
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PurchaseItemResponseDTO.registerPurchaseItemResponseDTO> registerPurchaseItem(
            @RequestPart("data") @Valid PurchaseItemRequestDTO dto,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @AuthUser Long userId
    ) {
        Long savedItemId = purchaseItemService.registerItem(dto, imageFiles, userId);
        return ApiResponse.onSuccess(PurchaseItemConverter.toRegisterPurchaseItemResponseDTO(savedItemId));
    }


    // 상품 검색
    @Operation(
            summary = "같이사요 상품 검색",
            description = """
            키워드로 상품을 검색합니다.
            """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<PurchaseItemListDTO>>> searchItems(
            @RequestParam String keyword,
            @AuthUser Long userId
    ) {
        List<PurchaseItemListDTO> result = purchaseItemService.searchItems(keyword, userId);
        log.info("keyword: [{}]", keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

    // 상품검색(상품명)
    @GetMapping("/title")
    @Operation(
            summary = "상품명 기반 지역 설정 검색",
            description = "- 사용자가 설정한 3개 지역 내에서만 상품명을 기준으로 검색"
    )
    public ApiResponse<List<PurchaseItemListDTO>> searchByTitleWithUserRegion(
            @RequestParam String keyword,
            @AuthUser Long userId
    ) {
        List<PurchaseItemListDTO> results = purchaseItemService.searchByTitleInUserRegions(keyword, userId);
        return ApiResponse.onSuccess(results);
    }


    // 단일 상품 검색
    @Operation(
            summary = "같이사요 단일 상품조회",
            description = """
            상품의 상세조회를 합니다.
            
            - `#태그명` : 설정한 3개 지역 내 태그로 검색
            - `카테고리명` : 설정한 3개 지역 내 카테고리로 검색
            - `지역명` : 설정과 무관하게 특정 지역명으로 검색
            """
    )
  
    @GetMapping("/{groupPurchaseId}")

    public ApiResponse<PurchaseItemResponseDTO.GetPurchaseItemResponseDTO> getGroupPurchaseDetail(
            @PathVariable Long groupPurchaseId,
            @AuthUser Long userId
    ) {
        PurchaseItemResponseDTO.GetPurchaseItemResponseDTO detail =
                purchaseItemService.getItemDetail(groupPurchaseId, userId);
        return ApiResponse.onSuccess(detail);
    }

    @Operation(
            summary = "같이사요 스크랩 등록",
            description = "특정 상품을 스크랩합니다. 이미 스크랩한 경우 예외 발생"
    )
    @PostMapping("/{groupPurchaseId}/scrap")
    public ApiResponse<Void> addScrap(
            @PathVariable("groupPurchaseId") Long purchaseItemId,
            @AuthUser Long userId
    ) {
        purchaseItemService.addScrap(purchaseItemId, userId);
        return ApiResponse.onSuccess(null);
    }
    @DeleteMapping("/{purchaseItemId}/scrap")
    @Operation(summary = "같이사요 상품 스크랩 삭제")
    public ApiResponse<Void> removeScrap(
            @PathVariable("purchaseItemId") Long purchaseItemId,
            @AuthUser Long userId
    ) {
        purchaseItemService.removeScrap(purchaseItemId, userId);
        return ApiResponse.onSuccess(null);
    }
}

