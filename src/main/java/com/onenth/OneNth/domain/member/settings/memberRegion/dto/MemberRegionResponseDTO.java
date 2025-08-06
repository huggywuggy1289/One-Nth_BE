package com.onenth.OneNth.domain.member.settings.memberRegion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MemberRegionResponseDTO {

    @Getter
    @Builder
    public static class AddMyRegionResponseDTO {
        private Integer regionId;
        private String regionName;
        private boolean isMain;
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
        private boolean isMain;
    }

    @Getter
    @Builder
    public static class UpdateMainRegionResponseDTO {
        private Integer regionId;
        private String regionName;
        private boolean isMain;
    }

    @Getter
    @Builder
    public static class VerifyMyRegionResponseDTO {
        private boolean isVerified;
        private String detectedRegionName;
        private String requestedRegionName;
    }

    @Getter
    @NoArgsConstructor
    public static class KakaoRegionResponseDTO {
        private List<Document> documents;

        @Getter
        @NoArgsConstructor
        public static class Document {
            @JsonProperty("region_type")
            private String regionType;

            @JsonProperty("region_1depth_name")
            private String region1DepthName;

            @JsonProperty("region_2depth_name")
            private String region2DepthName;

            @JsonProperty("region_3depth_name")
            private String region3DepthName;
        }

    }

    @Getter
    @Builder
    public static class GetRegionsByKeywordResponseDTO {
        private List<SearchedRegionSummary> regions;
        private Pagination pagination;
    }

    @Getter
    @Builder
    public static class SearchedRegionSummary {
        private Integer regionId;
        private String regionName;
    }

    @Getter
    @Builder
    public static class Pagination {
        private int page;
        private int size;
        private int totalPages;
        private long totalElements;
        private boolean isLast;
    }
}
