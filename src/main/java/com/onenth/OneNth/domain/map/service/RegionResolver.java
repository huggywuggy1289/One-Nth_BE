package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import com.onenth.OneNth.global.external.kakao.service.GeoCodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegionResolver {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final GeoCodingService geoCodingService;

    public Region resolveRegion(Long userId, Long regionId) {

        if (regionId != null) {
            return regionRepository.findById(regionId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_NOT_FOUND));
        } else {
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

            MemberRegion mainMemberRegion = memberRegionRepository.findByMemberAndIsMainTrue(member)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.MAIN_REGION_NOT_FOUND));

            return mainMemberRegion.getRegion();
        }
    }

    public Region resolveRegion(double lat, double lng) {
        String regionName = geoCodingService.getRegionNameByCoordinates(lat, lng);

        return regionRepository.findByRegionName(regionName)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_NOT_FOUND));
    }
}
