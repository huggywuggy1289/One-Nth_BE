package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.product.dto.SharingItemRequestDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;

import java.time.LocalDate;
import java.util.List;

public class SharingItemConverter {

    public static SharingItemRequestDTO toRequestDTO(
            String title,
            Integer quantity,
            Integer price,
            ItemCategory itemCategory,
            LocalDate expirationDate,
            Boolean isAvailable,
            PurchaseMethod purchaseMethod,
            Long regionId,
            List<String> tags
    ) {
        return SharingItemRequestDTO.builder()
                .title(title)
                .quantity(quantity)
                .price(price)
                .itemCategory(itemCategory)
                .expirationDate(expirationDate)
                .isAvailable(isAvailable)
                .purchaseMethod(purchaseMethod)
                .regionId(regionId)
                .tags(tags)
                .build();
    }

    public static SharingItemResponseDTO.RegisterResponse toRegisterResponse(Long id) {
        return SharingItemResponseDTO.RegisterResponse.builder()
                .sharingItem(id)
                .build();
    }
}

