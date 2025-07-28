package com.onenth.OneNth.domain.product.controller;

import com.onenth.OneNth.domain.product.converter.SharingItemConverter;
import com.onenth.OneNth.domain.product.dto.SharingItemListDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemRequestDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.service.SharingItemService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
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
    - keyword에 해당하는 상품명을 LIKE 검색합니다.
    - regionIds 파라미터에 선택한 지역 ID 리스트를 넘기면 해당 지역만 필터링합니다.
    - 지역 선택을 안 하면 전국 검색입니다.
    """
    )

    @GetMapping("/title")
    public ApiResponse<List<SharingItemListDTO>> searchByTitleWithRegionFilter(
            @RequestParam String keyword,
            @RequestParam(required = false) List<Integer> regionIds,
            @AuthUser Long userId
    ) {
        List<SharingItemListDTO> results = sharingItemService.searchByTitleAndSelectedRegions(keyword, regionIds);
        return ApiResponse.onSuccess(results);
    }

}
