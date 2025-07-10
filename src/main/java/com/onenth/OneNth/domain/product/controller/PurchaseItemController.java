package com.onenth.OneNth.domain.product.controller;

import com.onenth.OneNth.domain.product.DTO.PurchaseItemRequestDTO;
import com.onenth.OneNth.domain.product.DTO.PurchaseItemRequestDTO;
import com.onenth.OneNth.domain.product.service.PurchaseItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/group-purchases")
@RequiredArgsConstructor
public class PurchaseItemController {

    private final PurchaseItemService purchaseItemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> registerPurchaseItem(
            @RequestParam("title") String title,
            @RequestParam("purchaseMethod") String purchaseMethod,
            @RequestParam("itemCategory") String itemCategory,
            @RequestParam("purchaseUrl") String purchaseUrl,
            @RequestParam(value = "expirationDate", required = false) String expirationDate, // 필수 여부 X
            @RequestParam("originPrice") Integer originPrice,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles
    ) {
        Long dummyUserId = 1L;

        Long savedItemId = purchaseItemService.registerItem(
                title, purchaseMethod, itemCategory, purchaseUrl,
                expirationDate, originPrice, imageFiles, dummyUserId);

        return ResponseEntity.ok(savedItemId);
    }



}

