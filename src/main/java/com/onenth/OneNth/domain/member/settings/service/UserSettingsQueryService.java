package com.onenth.OneNth.domain.member.settings.service;

import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;

public interface UserSettingsQueryService {

    UserSettingsResponseDTO.MyRegionListResponseDTO getMyRegions(Long userId);

}
