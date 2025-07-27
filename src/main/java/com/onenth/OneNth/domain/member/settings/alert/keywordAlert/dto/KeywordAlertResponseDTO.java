package com.onenth.OneNth.domain.member.settings.alert.keywordAlert.dto;

import com.onenth.OneNth.domain.member.entity.enums.KeywordAlertType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class KeywordAlertResponseDTO {

    @Getter
    @Builder
    public static class AddKeywordAlertResponseDTO {
        private KeywordAlertType keywordAlertType;
        private Long keywordAlertId;
        private String keyword;
        private boolean enabled;
    }

    @Getter
    @Builder
    public static class SetKeywordAlertStatusResponseDTO {
        private KeywordAlertType keywordAlertType;
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
