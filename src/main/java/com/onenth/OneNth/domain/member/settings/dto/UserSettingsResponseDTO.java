package com.onenth.OneNth.domain.member.settings.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class UserSettingsResponseDTO {

    @Getter
    @Builder
    public static class AddMyRegionResponseDTO {
        private Integer regionId;
        private String regionName;
    }

    @Getter
    @Builder
    public static class MyRegionListResponseDTO {
        private List<RegionSummary> myRegions;
    }

    @Getter
    @Builder
    public static class RegionSummary {
        private Integer regionId;
        private String regionName;
    }

    @Getter
    @Builder
    public static class UpdateMainRegionResponseDTO {
        private Integer regionId;
        private String regionName;
        private boolean isMain;
    }
}
