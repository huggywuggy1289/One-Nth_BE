package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.product.dto.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.dto.PurchaseItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PurchaseItemConverter {

    public static PurchaseItemResponseDTO.registerPurchaseItemResponseDTO toRegisterPurchaseItemResponseDTO(Long purchaseItemId){
        return PurchaseItemResponseDTO.registerPurchaseItemResponseDTO.builder()
                .purchaseItem(purchaseItemId)
                .build();
    }

    // 북마크 여부 포함 변환
    public static List<PurchaseItemListDTO> toPurchaseItemListDTOs(
            List<PurchaseItem> items,
            Set<Long> bookmarkedIds,
            Map<Long, List<String>> imageMap
    ) {
        return items.stream()
                .map(item -> {
                    List<String> urls = imageMap.getOrDefault(item.getId(), Collections.emptyList());

                    return PurchaseItemListDTO.builder()
                            .id(item.getId())
                            .category(item.getItemCategory().name())
                            .title(item.getName())
                            .price(String.valueOf(item.getPrice()))
                            .purchaseMethod(item.getPurchaseMethod())  // ONLINE / OFFLINE
                            .imageUrls(urls)
                            .status(item.getStatus().name()) // +
                            .bookmarked(bookmarkedIds.contains(item.getId()))
                            .latitude(item.getLatitude())
                            .longitude(item.getLongitude())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
