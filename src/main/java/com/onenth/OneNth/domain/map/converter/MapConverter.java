package com.onenth.OneNth.domain.map.converter;

import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.map.enums.MarkerType;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;

import java.util.List;
import java.util.stream.Collectors;

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

    public static MapResponseDTO.GetItemMarkerDetailsResponseDTO toGetItemMarkerDetailsResponseDTO(List<MapResponseDTO.ItemMarkerDetail> itemMarkerDetails) {
        return MapResponseDTO.GetItemMarkerDetailsResponseDTO.builder()
                .itemMarkerDetails(itemMarkerDetails)
                .build();
    }

    public static MapResponseDTO.ItemMarkerDetail toItemMarkerDetail(PurchaseItem item, boolean isVerified) {
        List<String> imageUrls = item.getItemImages().stream()
                .map(ItemImage::getUrl).collect(Collectors.toList());

        return MapResponseDTO.ItemMarkerDetail.builder()
                .status(item.getStatus())
                .itemCategory(item.getItemCategory())
                .purchaseMethod(item.getPurchaseMethod())
                .isScraped(isVerified)
                .imageUrls(imageUrls)
                .title(item.getName())
                .price(item.getPrice())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .build();
    }

    public static MapResponseDTO.ItemMarkerDetail toItemMarkerDetail(SharingItem item, boolean isVerified) {
        List<String> imageUrls = item.getItemImages().stream()
                .map(ItemImage::getUrl).collect(Collectors.toList());

        return MapResponseDTO.ItemMarkerDetail.builder()
                .status(item.getStatus())
                .itemCategory(item.getItemCategory())
                .purchaseMethod(item.getPurchaseMethod())
                .isScraped(isVerified)
                .imageUrls(imageUrls)
                .title(item.getTitle())
                .price(item.getPrice())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .build();
    }

    public static MapResponseDTO.GetPostMarkerDetailsResponseDTO toGetPostMarkerDetailsResponseDTO(List<MapResponseDTO.PostMarkerDetail> postMarkerDetails) {
        return MapResponseDTO.GetPostMarkerDetailsResponseDTO.builder()
                .postMarkerDetails(postMarkerDetails)
                .build();
    }

    public static MapResponseDTO.PostMarkerDetail toPostMarkerDetail(Post post, boolean isVerified) {
        return MapResponseDTO.PostMarkerDetail.builder()
                .placeName(post.getPlaceName())
                .isScraped(isVerified)
                .title(post.getTitle())
                .address(post.getAddress())
                .createdAt(post.getCreatedAt())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .build();
    }
}
