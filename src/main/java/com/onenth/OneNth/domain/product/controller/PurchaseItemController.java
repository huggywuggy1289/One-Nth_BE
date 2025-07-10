package com.onenth.OneNth.domain.product.controller;

import com.onenth.OneNth.domain.product.DTO.PurchaseItemRequestDTO;
import com.onenth.OneNth.domain.product.DTO.PurchaseItemRequestDTO;
import com.onenth.OneNth.domain.product.service.PurchaseItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group-purchases")
@RequiredArgsConstructor
public class PurchaseItemController {

    private final PurchaseItemService purchaseItemService;

    @PostMapping
    public ResponseEntity<Long> registerPurchaseItem(
            @Valid @RequestBody PurchaseItemRequestDTO dto) {

        // TODO: 실제 유저 ID는 인증 구현 후 SecurityContext 등에서 가져와야함.
        Long dummyUserId = 1L;
        Long savedItemId = purchaseItemService.registerItem(dto, dummyUserId);
        return ResponseEntity.ok(savedItemId);
    }

}

