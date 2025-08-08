package com.onenth.OneNth.domain.map.dto;

import com.onenth.OneNth.domain.map.enums.MarkerType;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class MapResponseDTO {

    @Getter
    @Builder
    public static class GetMarkersResponseDTO {
        private List<GroupedMarkerSummary> groupedMarkers;
    }

    @Getter
    @Builder
    public static class GroupedMarkerSummary {
        private Double latitude;
        private Double longitude;
        private List<MapResponseDTO.MarkerSummary> markers;
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

    @Getter
    @Builder
    public static class GetItemMarkerDetailsResponseDTO {
        private List<ItemMarkerDetail> itemMarkerDetails;
    }

    @Getter
    @Builder
    public static class ItemMarkerDetail {
        private Status status;
        private ItemCategory itemCategory;
        private PurchaseMethod purchaseMethod;
        private boolean isScraped;
        private List<String> imageUrls;
        private String title;
        private Integer price;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @Builder
    public static class GetPostMarkerDetailsResponseDTO {
        private List<PostMarkerDetail> postMarkerDetails;
    }

    @Getter
    @Builder
    public static class PostMarkerDetail {
        private String placeName;
        private boolean isScraped;
        private String title;
        private String address;
        private LocalDateTime createdAt;
        private Double latitude;
        private Double longitude;
    }
}
