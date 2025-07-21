package com.onenth.OneNth.domain.alert.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AlertRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class SetRegionAlertStatusRequestDTO {

        @Schema(description = "알람 활성화 여부", example = "true")
        @NotNull(message = "알림 활성화 여부는 필수입니다.")
        private Boolean isEnabled;

    }
}
