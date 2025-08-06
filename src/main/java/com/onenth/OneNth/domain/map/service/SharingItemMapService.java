package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharingItemMapService implements MapService {

    private final SharingItemRepository sharingItemRepository;
    private final RegionResolver regionResolver;

    @Override
    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId) {

        Region region = regionResolver.resolveRegion(userId, regionId);

        List<SharingItem> sharingItems = sharingItemRepository.findAllByRegionAndPurchaseMethod(region, PurchaseMethod.OFFLINE);

        List<MapResponseDTO.MarkerSummary> summaries = sharingItems.stream().map(
                sharingItem -> MapConverter.toMarkerSummary(sharingItem)
        ).collect(Collectors.toList());

        return MapConverter.toGetMarkersResponseDTO(summaries);
    }
}
