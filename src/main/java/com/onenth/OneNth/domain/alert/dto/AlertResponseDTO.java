package com.onenth.OneNth.domain.alert.dto;

import com.onenth.OneNth.domain.alert.entity.enums.KeywordAlertType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

    @Getter
    @Builder
    public static class SetKeywordAlertStatusResponseDTO {
        private Long keywordAlertId;
        private String keyword;
        private boolean enabled;
    }

    @Getter
    @Builder
    public static class AlertListResponseDTO {
        private List<AlertSummary> alertSummaryList;
    }

    @Getter
    @Builder
    public static class AlertSummary {
        private KeywordAlertType keywordAlertType;
        private Long alertId;
        private String keyword;
        private boolean enabled;
    }
}
