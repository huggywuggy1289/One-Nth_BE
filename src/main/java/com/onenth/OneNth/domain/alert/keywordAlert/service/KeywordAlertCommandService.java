package com.onenth.OneNth.domain.alert.keywordAlert.service;

import com.onenth.OneNth.domain.alert.keywordAlert.dto.KeywordAlertRequestDTO;
import com.onenth.OneNth.domain.alert.keywordAlert.dto.KeywordAlertResponseDTO;

public interface KeywordAlertCommandService {

    KeywordAlertResponseDTO.AddKeywordAlertResponseDTO addRegionKeyword(Long userId, Long regionId);

    KeywordAlertResponseDTO.SetKeywordAlertStatusResponseDTO setRegionAlertStatus(
            Long userId,
            Long regionKeywordAlertId,
            KeywordAlertRequestDTO.SetRegionAlertStatusRequestDTO request
    );

    KeywordAlertResponseDTO.AddKeywordAlertResponseDTO addProductKeyword(Long userId, KeywordAlertRequestDTO.AddKeywordAlertRequestDTO request);

    KeywordAlertResponseDTO.SetKeywordAlertStatusResponseDTO setProductKeywordAlertStatus(
            Long userId,
            Long productKeywordAlertId,
            KeywordAlertRequestDTO.SetKeywordAlertStatusRequestDTO request
    );

    KeywordAlertResponseDTO.AlertListResponseDTO updateKeywordAlertList(
            Long userId,
            KeywordAlertRequestDTO.UpdateKeywordAlertListRequestDTO request
    );
}
