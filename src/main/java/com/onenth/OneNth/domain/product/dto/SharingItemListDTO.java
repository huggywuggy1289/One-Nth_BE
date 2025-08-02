package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.SharingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class SharingItemListDTO {

    private Long id;                // 상품 ID
    private String category;        // 카테고리명 (Enum -> name)
    private String title;           // 상품명
    private String price;           // 가격 (정수 -> 문자열 변환)
    private String thumbnailUrl;    // 썸네일 이미지 URL
    private boolean isBookmarked;   // 북마크 여부 (스크랩 여부)

    private Double latitude;
    private Double longitude;

    public static SharingItemListDTO fromEntity(SharingItem entity) {
        return SharingItemListDTO.builder()
                .id(entity.getId())
                .category(entity.getItemCategory().name())
                .title(entity.getTitle())
                .price(String.valueOf(entity.getPrice()))
                .thumbnailUrl(
                        entity.getItemImages() != null && !entity.getItemImages().isEmpty()
                                ? entity.getItemImages().get(0).getUrl()
                                : null
                )
                .latitude(entity.getLongitude())
                .longitude(entity.getLongitude())
                .isBookmarked(false)
                .build();
    }
}
