package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

import java.util.List;

public interface MapService {

    MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId);

}
