package com.onenth.OneNth.domain.member.settings.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.settings.converter.UserSettingsConverter;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import jakarta.persistence.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSettingsQueryServiceImpl implements UserSettingsQueryService {

    private final MemberRepository memberRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final RegionRepository regionRepository;

    @Override
    public UserSettingsResponseDTO.MyRegionListResponseDTO getMyRegions(Long userId) {

        Member member = memberRepository.findByIdWithRegions(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<MemberRegion> memberRegionList = memberRegionRepository.findAllByMember(member);

        List<UserSettingsResponseDTO.RegionSummary> regionSummaryList = memberRegionList.stream()
                .map(memberRegion ->
                        UserSettingsConverter.toRegionSummary(memberRegion)
                ).collect(Collectors.toList());

        return UserSettingsConverter.toGetMyRegionsResponseDTO(regionSummaryList);
    }

    @Override
    public UserSettingsResponseDTO.GetRegionsByKeywordResponseDTO getRegionsByKeyword(String keyword, Pageable pageable) {

        Page<Region> regionPage = regionRepository.findByRegionNameContaining(keyword, pageable);

        List<UserSettingsResponseDTO.SearchedRegionSummary> regionSummaryList = regionPage.getContent().stream()
                .map(region -> UserSettingsConverter.toSearchedRegionSummary(region))
                .collect(Collectors.toList());

        UserSettingsResponseDTO.Pagination pagination = UserSettingsConverter.toPagination(regionPage);

        return UserSettingsConverter.toGetRegionsByKeywordResponseDTO(regionSummaryList, pagination);
    }

}
