package com.onenth.OneNth.domain.member.settings.profile.service;

import com.onenth.OneNth.domain.member.settings.profile.dto.ProfileResponseDTO;

public interface ProfileQueryService {

    ProfileResponseDTO.GetMyProfileResponseDTO getMyProfile(Long userId);
}
