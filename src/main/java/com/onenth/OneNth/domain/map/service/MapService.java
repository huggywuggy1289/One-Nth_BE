package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.dto.MapResponseDTO;

import java.util.List;

public interface MapService {

    MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId);

}
