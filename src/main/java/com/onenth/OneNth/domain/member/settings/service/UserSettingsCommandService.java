package com.onenth.OneNth.domain.member.settings.service;

import com.onenth.OneNth.domain.member.settings.dto.UserSettingsRequestDTO;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;

public interface UserSettingsCommandService {

    UserSettingsResponseDTO.AddMyRegionResponseDTO addMyRegion(Long userId, UserSettingsRequestDTO.AddMyRegionRequestDTO request);
}
