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

    @Getter
    @NoArgsConstructor
    public static class AddKeywordAlertRequestDTO {

        @Schema(description = "알람으로 등록할 키워드", example = "욕실청소")
        @NotNull(message = "키워드로 등록할 단어를 입력해주세요.")
        private String keyword;

    }

    @Getter
    @NoArgsConstructor
    public static class SetKeywordAlertStatusRequestDTO {

        @Schema(description = "알람 활성화 여부", example = "true")
        @NotNull(message = "알림 활성화 여부는 필수입니다.")
        private Boolean isEnabled;

    }
}
