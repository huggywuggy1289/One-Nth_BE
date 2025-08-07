package com.onenth.OneNth.domain.deal.dto;

import lombok.*;

public class DealResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealConfirmationResponseDTO {
        private Long dealConfirmationFormId;
        private String writerName;
        private String itemName;
        private String content = "거래확정 폼 작성 됨";
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealCompletionResponseDTO {
        private Long dealCompletionFormId;
        private String writerName;
        private String itemName;
        private String content = "거래완료 폼이 작성 됨";
    }
}
