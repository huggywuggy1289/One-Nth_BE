package com.onenth.OneNth.domain.member.settings.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserSettingsRequestDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddMyRegionRequestDTO {
        @NotNull
        private Long regionId;
    }
}
