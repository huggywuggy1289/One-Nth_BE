package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import com.onenth.OneNth.global.external.kakao.dto.GeoCodingResult;
import com.onenth.OneNth.global.external.kakao.service.GeoCodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapRegionService {
    private final RegionResolver regionResolver;
    private final GeoCodingService geoCodingService;
    private final RegionRepository regionRepository;

    public MapResponseDTO.GetRegionByCoordinatesResponseDTO getRegionByCoordinates(double lat, double lng) {
        Region region = regionResolver.resolveRegion(lat, lng);

        return MapResponseDTO.GetRegionByCoordinatesResponseDTO.builder()
                .regionId(region.getId())
                .regionName(region.getRegionName())
                .build();
    }

    public MapResponseDTO.GetCoordinatesByNameResponseDTO getRegionCoordinatesByName(String regionName) {
        Region region = regionRepository.findByRegionName(regionName)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_NOT_FOUND));

        GeoCodingResult geoCodingResult = geoCodingService.getCoordinatesFromAddress(regionName);

        return MapResponseDTO.GetCoordinatesByNameResponseDTO.builder()
                .regionId(region.getId())
                .regionName(region.getRegionName())
                .longitude(geoCodingResult.getLongitude())
                .latitude(geoCodingResult.getLatitude())
                .build();
    }
}
