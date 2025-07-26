package com.onenth.OneNth.domain.member.settings.alert.generalAlert.dto;

import com.onenth.OneNth.domain.alert.entity.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GeneralAlertResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetScrapAlertStatusResponseDTO {
        AlertType alertType;
        boolean enabled;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetReviewAlertStatusResponseDTO {
        AlertType alertType;
        boolean enabled;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SetChatAlertStatusResponseDTO {
        AlertType alertType;
        boolean enabled;
    }
}
