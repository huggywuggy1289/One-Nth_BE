package com.onenth.OneNth.domain.member.settings.alert.generalAlert.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GeneralAlertRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class SetScrapAlertStatusRequestDTO {

        @Schema(description = "스크랩 알림 활성화 여부", example = "true")
        @NotNull(message = "알림 활성화 여부는 필수입니다.")
        private Boolean isEnabled;

    }
}
