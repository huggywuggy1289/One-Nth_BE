package com.onenth.OneNth.domain.product.DTO;

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
}
