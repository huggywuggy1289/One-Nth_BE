package com.onenth.OneNth.domain.member.settings.memberRegion.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRegionRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class AddMyRegionRequestDTO {
        @NotNull
        private Long regionId;
    }

    @Getter
    @NoArgsConstructor
    public static class VerifyMyRegionRequestDTO {
        @NotNull
        @DecimalMin(value = "-90.0", message = "유효하지 않은 위도 값입니다.")
        @DecimalMax(value = "90.0", message = "유효하지 않은 위도 값입니다.")
        private Double latitude;

        @NotNull
        @DecimalMin(value = "-180.0", message = "유효하지 않은 경도 값입니다.")
        @DecimalMax(value = "180.0", message = "유효하지 않은 경도 값입니다.")
        private Double longitude;
    }
}
