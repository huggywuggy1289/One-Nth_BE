package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapRegionService {
    private final RegionResolver regionResolver;

    public MapResponseDTO.GetRegionByCoordinatesResponseDTO getRegionByCoordinates(double lat, double lng) {
        Region region = regionResolver.resolveRegion(lat, lng);

        return MapResponseDTO.GetRegionByCoordinatesResponseDTO.builder()
                .regionId(region.getId())
                .regionName(region.getRegionName())
                .build();
    }
}
