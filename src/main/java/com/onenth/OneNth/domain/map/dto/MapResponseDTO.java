package com.onenth.OneNth.domain.map.dto;

import com.onenth.OneNth.domain.map.enums.MarkerType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MapResponseDTO {

    @Getter
    @Builder
    public static class GetMarkersResponseDTO {
        private List<MarkerSummary> markers;
    }

    @Getter
    @Builder
    public static class MarkerSummary {
        private MarkerType markerType;
        private Long id;
        private String title;
        private Double latitude;
        private Double longitude;
    }
}
