package com.onenth.OneNth.domain.product.controller;

import com.onenth.OneNth.domain.product.converter.SharingItemConverter;
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

    @Operation(
            summary = "함께 나눠요 상품 등록 API",
            description = "함께 나눠요 상품을 등록합니다. \n\n" +
                    "- 소비기한은 식품 카테고리(FOOD)일 때만 필수입니다.\n" +
                    "- 이미지는 최소 1장 이상, 최대 3장까지 첨부 가능합니다.\n" +
                    "- 태그는 반드시 #으로 시작해야 합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SharingItemResponseDTO.RegisterResponse> registerSharingItem(
            @RequestParam("title") String title,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("price") Integer price,
            @RequestParam("itemCategory") ItemCategory itemCategory,
            @RequestParam(value = "expirationDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expirationDate,
            @RequestParam("isAvailable") Boolean isAvailable,
            @RequestParam("purchaseMethod") PurchaseMethod purchaseMethod,
            @RequestParam("regionId") Long regionId,
            @RequestParam("tags") List<String> tags,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @AuthUser Long userId
    ) {
        SharingItemRequestDTO dto = SharingItemConverter.toRequestDTO(
                title, quantity, price, itemCategory, expirationDate,
                isAvailable, purchaseMethod, regionId, tags
        );

        Long savedId = sharingItemService.registerItem(dto, imageFiles, userId);
        return ApiResponse.onSuccess(SharingItemConverter.toRegisterResponse(savedId));
    }



}
