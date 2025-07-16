package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PurchaseItemListDTO {
    private Long id;                // 상품 ID
    private String category;       // 카테고리명
    private String title;          // 상품명
    private String price;          // 가격
    private String thumbnailUrl;   // 썸네일 이미지 URL
    private boolean isBookmarked;  // 북마크(스크랩) 여부

    // 서비스에서 dto변환 로직이 길어지는 것을 방지하기위해 이쪽에 배치
    public static PurchaseItemListDTO fromEntity(PurchaseItem entity) {
        return PurchaseItemListDTO.builder()
                .id(entity.getId())
                .category(entity.getItemCategory().name())
                .title(entity.getName())
                .price(String.valueOf(entity.getPrice()))
                .thumbnailUrl(
                        entity.getItemImages() != null && !entity.getItemImages().isEmpty()
                                ? entity.getItemImages().get(0).getUrl()
                                : null
                )
                .isBookmarked(false) // TODO: 북마크 여부는 추후 구현
                .build();
    }
}
