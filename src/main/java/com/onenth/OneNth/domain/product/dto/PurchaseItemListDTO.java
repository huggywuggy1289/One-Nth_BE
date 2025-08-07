package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PurchaseItemListDTO {
    private Long id;                // 상품 ID
    private String category;       // 카테고리명
    private String title;          // 상품명
    private String price;          // 가격
    private String thumbnailUrl;   // 썸네일 이미지 URL
    private boolean bookmarked;  // 북마크(스크랩) 여부

    private Double latitude;
    private Double longitude;

    private String status;
    private String statusLabel;

    public static PurchaseItemListDTO fromEntity(PurchaseItem entity) {
        return fromEntity(entity, false); // 기본값 false로 위임
    }

    // 서비스에서 dto변환 로직이 길어지는 것을 방지하기위해 이쪽에 배치
    public static PurchaseItemListDTO fromEntity(PurchaseItem entity, boolean isBookmarked) {
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
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .bookmarked(isBookmarked)
                .status(entity.getStatus().name())
                .statusLabel(toStatusLabel(entity.getStatus()))
                .build();
    }
    public static String toStatusLabel(Status s) {
        return switch (s) {
            case DEFAULT -> "판매중";
            case IN_PROGRESS -> "거래확정";
            case COMPLETED -> "거래완료";
        };
    }
}
