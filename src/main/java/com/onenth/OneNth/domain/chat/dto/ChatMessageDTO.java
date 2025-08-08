package com.onenth.OneNth.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatMessageDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDTO {
        private Long sendMemberId;
        private String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealConfirmationResponseDTO {
        private Long dealConfirmationFormId;
        private Long sendMemberId;
        private String itemName;

        @Builder.Default
        private String content = "거래확정 폼이 작성 됨";
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealCompletionResponseDTO {
        private Long dealCompletionFormId;
        private Long sendMemberId;
        private String itemName;

        @Builder.Default
        private String content = "거래완료 폼이 작성 됨";
    }
}