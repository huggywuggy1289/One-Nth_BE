package com.onenth.OneNth.domain.map.converter;

import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.map.enums.MarkerType;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;

import java.util.List;

public class MapConverter {

    public static MapResponseDTO.GetMarkersResponseDTO toGetMarkersResponseDTO(List<MapResponseDTO.MarkerSummary> summaries) {
        return MapResponseDTO.GetMarkersResponseDTO.builder()
                .markers(summaries)
                .build();
    }

    public static MapResponseDTO.MarkerSummary toMarkerSummary(PurchaseItem item) {
        return MapResponseDTO.MarkerSummary.builder()
                .markerType(MarkerType.PURCHASEITEM)
                .id(item.getId())
                .title(item.getName())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .build();
    }

    public static MapResponseDTO.MarkerSummary toMarkerSummary(SharingItem item) {
        return MapResponseDTO.MarkerSummary.builder()
                .markerType(MarkerType.SHARINGITEM)
                .id(item.getId())
                .title(item.getTitle())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .build();
    }

    public static MapResponseDTO.MarkerSummary toMarkerSummary(Post post, MarkerType markerType) {
        return MapResponseDTO.MarkerSummary.builder()
                .markerType(markerType)
                .id(post.getId())
                .title(post.getTitle())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .build();
    }
}
