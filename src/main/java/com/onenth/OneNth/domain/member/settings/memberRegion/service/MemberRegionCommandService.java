package com.onenth.OneNth.domain.member.settings.memberRegion.service;

import com.onenth.OneNth.domain.member.settings.memberRegion.dto.MemberRegionRequestDTO;
import com.onenth.OneNth.domain.member.settings.memberRegion.dto.MemberRegionResponseDTO;

public interface MemberRegionCommandService {

    MemberRegionResponseDTO.AddMyRegionResponseDTO addMyRegion(Long userId, MemberRegionRequestDTO.AddMyRegionRequestDTO request);

    void deleteMyRegion(Long userId, Long regionId);

    MemberRegionResponseDTO.UpdateMainRegionResponseDTO updateMainRegion(Long userId, Long regionId);

    MemberRegionResponseDTO.VerifyMyRegionResponseDTO verifyMyRegion(Long userId, Long regionId, MemberRegionRequestDTO.VerifyMyRegionRequestDTO request);
}
