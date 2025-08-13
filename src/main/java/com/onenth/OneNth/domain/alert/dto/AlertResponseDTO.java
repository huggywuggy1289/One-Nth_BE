package com.onenth.OneNth.domain.alert.dto;

import com.onenth.OneNth.domain.alert.entity.AlertType;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AlertResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealNotificationResponseDTO {
        private AlertType alertType; // 상품 or 거래후기
        private ItemType itemType; // 함께 나눠요 or 같이 사요
        private Long contentId;
        private String message; // 알림 문구
        private Boolean readStatus; // 읽음 여부
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostNotificationResponseDTO {
        private AlertType alertType; // 생활, 할인, 맛집 정보
        private Long contentId; // 게시글 Id
        private String message; // 내용
        private Boolean readStatus; // 읽음 여부
    }
}