package com.onenth.OneNth.domain.alert.service;

import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;

public interface AlertCommandService {
    AlertResponseDTO.AddAlertResponseDTO addRegionKeyword(Long userId, Long regionId);
}
