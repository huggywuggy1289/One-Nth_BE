package com.onenth.OneNth.domain.alert.service;

import com.onenth.OneNth.domain.alert.dto.AlertRequestDTO;
import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;

public interface AlertCommandService {

    AlertResponseDTO.AddRegionAlertResponseDTO addRegionKeyword(Long userId, Long regionId);

    AlertResponseDTO.SetRegionAlertStatusResponseDTO setRegionAlertStatus(
            Long userId,
            Long regionKeywordAlertId,
            AlertRequestDTO.SetRegionAlertStatusRequestDTO request
    );

    AlertResponseDTO.AddKeywordAlertResponseDTO addKeyword(Long userId, AlertRequestDTO.AddKeywordAlertRequestDTO request);

    AlertResponseDTO.SetKeywordAlertStatusResponseDTO setKeywordAlertStatus(
            Long userId,
            Long keywordAlertId,
            AlertRequestDTO.SetKeywordAlertStatusRequestDTO request
    );

    AlertResponseDTO.AlertListResponseDTO updateKeywordAlertList(
            Long userId,
            AlertRequestDTO.UpdateKeywordAlertListRequestDTO request
    );
}
