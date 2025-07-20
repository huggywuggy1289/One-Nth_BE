package com.onenth.OneNth.domain.alert.dto;

import lombok.Builder;
import lombok.Getter;

public class AlertResponseDTO {

    @Getter
    @Builder
    public static class AddAlertResponseDTO {
        private Long regionKeywordAlertId;
        private String regionKeywordName;
        private boolean enabled;
    }
}
