package com.onenth.OneNth.domain.member.settings.dto;

import lombok.Builder;
import lombok.Getter;

public class UserSettingsResponseDTO {

    @Getter
    @Builder
    public static class AddMyRegionResponseDTO {
        private Integer regionId;
        private String regionName;
    }
}
