package com.onenth.OneNth.domain.product.controller;

import com.onenth.OneNth.domain.product.converter.PurchaseItemConverter;
import com.onenth.OneNth.domain.product.dto.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.dto.PurchaseItemResponseDTO;
import com.onenth.OneNth.domain.product.service.PurchaseItemService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
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
public class PurchaseItemController {

    private final PurchaseItemService purchaseItemService;

    // 상품등록
    @Operation(
            summary = "같이 사요 상품 등록 API",
            description = "같이 사요 상품을 등록합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PurchaseItemResponseDTO.registerPurchaseItemResponseDTO> registerPurchaseItem(
            @RequestParam("title") String title,
            @RequestParam("purchaseMethod") String purchaseMethod,
            @RequestParam("itemCategory") String itemCategory,
            @RequestParam("purchaseUrl") String purchaseUrl,
            @RequestParam(value = "expirationDate", required = false) String expirationDate, // 필수 여부 X
            @RequestParam("originPrice") Integer originPrice,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam("tags") List<String> tags,
            @AuthUser Long userId) {

        Long savedItemId = purchaseItemService.registerItem(
                title, purchaseMethod, itemCategory, purchaseUrl,
                expirationDate, originPrice, imageFiles, tags, userId);

        return ApiResponse.onSuccess(PurchaseItemConverter.toRegisterPurchaseItemResponseDTO(savedItemId));
    }

    // 상품 검색
    @Operation(
            summary = "같이사요 상품 검색",
            description = """
            키워드로 상품을 검색합니다.
            
            - `#태그명` : 설정한 3개 지역 내 태그로 검색
            - `카테고리명` : 설정한 3개 지역 내 카테고리로 검색
            - `지역명` : 설정과 무관하게 특정 지역명으로 검색
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
}

