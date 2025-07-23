package com.onenth.OneNth.domain.alert.service;

import com.onenth.OneNth.domain.alert.dto.AlertRequestDTO;
import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;

public interface AlertCommandService {

    AlertResponseDTO.AddKeywordAlertResponseDTO addRegionKeyword(Long userId, Long regionId);

    AlertResponseDTO.SetKeywordAlertStatusResponseDTO setRegionAlertStatus(
            Long userId,
            Long regionKeywordAlertId,
            AlertRequestDTO.SetRegionAlertStatusRequestDTO request
    );

    AlertResponseDTO.AddKeywordAlertResponseDTO addProductKeyword(Long userId, AlertRequestDTO.AddKeywordAlertRequestDTO request);

    AlertResponseDTO.SetKeywordAlertStatusResponseDTO setProductKeywordAlertStatus(
            Long userId,
            Long productKeywordAlertId,
            AlertRequestDTO.SetKeywordAlertStatusRequestDTO request
    );

    AlertResponseDTO.AlertListResponseDTO updateKeywordAlertList(
            Long userId,
            AlertRequestDTO.UpdateKeywordAlertListRequestDTO request
    );
}
