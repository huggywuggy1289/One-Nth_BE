package com.onenth.OneNth.domain.member.settings.memberRegion.service;

import com.onenth.OneNth.domain.member.settings.memberRegion.dto.MemberRegionResponseDTO;
import org.springframework.data.domain.Pageable;

public interface MemberRegionQueryService {

    MemberRegionResponseDTO.MyRegionListResponseDTO getMyRegions(Long userId);

    MemberRegionResponseDTO.GetRegionsByKeywordResponseDTO getRegionsByKeyword(String keyword, Pageable pageable);
}
