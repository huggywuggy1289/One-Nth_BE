package com.onenth.OneNth.domain.member.settings.service;

import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSettingsQueryService {

    UserSettingsResponseDTO.MyRegionListResponseDTO getMyRegions(Long userId);

    UserSettingsResponseDTO.GetRegionsByKeywordResponseDTO getRegionsByKeyword(String keyword, Pageable pageable);
}
