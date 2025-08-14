package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class SharingItemListDTO {

    private Long id;                // 상품 ID
    private String category;        // 카테고리명 (Enum -> name)
    private String title;           // 상품명
    private String price;           // 가격 (정수 -> 문자열 변환)
    private String thumbnailUrl;    // 썸네일 이미지 URL
    private boolean bookmarked;   // 북마크 여부 (스크랩 여부)

    private Double latitude;
    private Double longitude;

    private String status;
    private String statusLabel;

    // +
    private PurchaseMethod purchaseMethod;
    private List<String> imageUrls;

    public static SharingItemListDTO fromEntity(SharingItem entity, boolean isBookmarked) {
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
