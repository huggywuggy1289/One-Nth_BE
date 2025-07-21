package com.onenth.OneNth.domain.alert.service;

import com.onenth.OneNth.domain.alert.dto.AlertRequestDTO;
import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;

public interface AlertCommandService {

    AlertResponseDTO.AddRegionAlertResponseDTO addRegionKeyword(Long userId, Long regionId);

    AlertResponseDTO.SetRegionAlertStatusResponseDTO setRegionAlertStatus(
            Long userId,
            Long regionKeywordAlertId,
            AlertRequestDTO.SetRegionAlertStatusRequestDTO request);

    AlertResponseDTO.AddKeywordAlertResponseDTO addKeyword(Long userId, AlertRequestDTO.AddKeywordAlertRequestDTO request);

}
