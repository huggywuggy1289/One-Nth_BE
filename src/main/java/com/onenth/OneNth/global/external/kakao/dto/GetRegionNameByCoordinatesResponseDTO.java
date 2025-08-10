package com.onenth.OneNth.global.external.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetRegionNameByCoordinatesResponseDTO {

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
