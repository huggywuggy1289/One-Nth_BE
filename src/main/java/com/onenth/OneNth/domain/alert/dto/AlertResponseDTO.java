package com.onenth.OneNth.domain.alert.dto;

import lombok.Builder;
import lombok.Getter;

public class AlertResponseDTO {

    @Getter
    @Builder
    public static class AddRegionAlertResponseDTO {
        private Long regionKeywordAlertId;
        private String regionKeywordName;
        private boolean enabled;
    }

    @Getter
    @Builder
    public static class SetRegionAlertStatusResponseDTO {
        private Long regionKeywordAlertId;
        private String regionKeywordName;
        private boolean enabled;
    }

    @Getter
    @Builder
    public static class AddKeywordAlertResponseDTO {
        private Long keywordAlertId;
        private String keyword;
        private boolean enabled;
    }

}
