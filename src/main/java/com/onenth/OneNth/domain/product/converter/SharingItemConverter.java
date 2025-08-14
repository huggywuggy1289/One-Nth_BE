package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.product.dto.SharingItemListDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemRequestDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharingItemConverter {

    public static SharingItemRequestDTO toRequestDTO(
            String title,
            Integer quantity,
            Integer price,
            ItemCategory itemCategory,
            LocalDate expirationDate,
            Boolean isAvailable,
            PurchaseMethod purchaseMethod,
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
                .tags(tags)
                .build();
    }

    public static SharingItemResponseDTO.RegisterResponse toRegisterResponse(Long id) {
        return SharingItemResponseDTO.RegisterResponse.builder()
                .sharingItem(id)
                .build();
    }

    public static List<SharingItemListDTO> toSharingItemListDTOs(
            List<SharingItem> items,
            Set<Long> bookmarkedIds
    ) {
        return items.stream()
                .filter(i -> i.getStatus() != Status.COMPLETED)
                .map(item -> SharingItemListDTO.fromEntity(item, bookmarkedIds.contains(item.getId())))
                .toList();
    }

    // ✅ 신규 오버로드: 이미지/구매방식/상태까지 포함
    // imageMap: key = sharingItemId, value = 이미지 URL 리스트
    public static List<SharingItemListDTO> toSharingItemListDTOs(
            List<SharingItem> items,
            Set<Long> bookmarkedIds,
            Map<Long, List<String>> imageMap
    ) {
        return items.stream()
                .filter(i -> i.getStatus() != Status.COMPLETED)
                .map(item -> {
                    List<String> urls = imageMap.getOrDefault(item.getId(), java.util.List.of());

                    return SharingItemListDTO.builder()
                            .id(item.getId())
                            .category(item.getItemCategory().name())
                            .title(item.getTitle())
                            .price(String.valueOf(item.getPrice()))
                            .purchaseMethod(item.getPurchaseMethod())  // ONLINE / OFFLINE
                            .imageUrls(urls)
                            .status(item.getStatus().name())
                            .bookmarked(bookmarkedIds.contains(item.getId()))
                            .latitude(item.getLatitude())
                            .longitude(item.getLongitude())
                            .build();
                })
                .toList();
    }
}

