package com.onenth.OneNth.domain.member.settings.memberRegion.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.settings.memberRegion.converter.MemberRegionConverter;
import com.onenth.OneNth.domain.member.settings.memberRegion.dto.MemberRegionResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
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
public class MemberRegionQueryServiceImpl implements MemberRegionQueryService {

    private final MemberRepository memberRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final RegionRepository regionRepository;

    @Override
    public MemberRegionResponseDTO.MyRegionListResponseDTO getMyRegions(Long userId) {

        Member member = memberRepository.findByIdWithRegions(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<MemberRegion> memberRegionList = memberRegionRepository.findAllByMember(member);

        List<MemberRegionResponseDTO.RegionSummary> regionSummaryList = memberRegionList.stream()
                .map(memberRegion ->
                        MemberRegionConverter.toRegionSummary(memberRegion)
                ).collect(Collectors.toList());

        return MemberRegionConverter.toGetMyRegionsResponseDTO(regionSummaryList);
    }

    @Override
    public MemberRegionResponseDTO.GetRegionsByKeywordResponseDTO getRegionsByKeyword(String keyword, Pageable pageable) {

        Page<Region> regionPage = regionRepository.findByRegionNameContaining(keyword, pageable);

        List<MemberRegionResponseDTO.SearchedRegionSummary> regionSummaryList = regionPage.getContent().stream()
                .map(region -> MemberRegionConverter.toSearchedRegionSummary(region))
                .collect(Collectors.toList());

        MemberRegionResponseDTO.Pagination pagination = MemberRegionConverter.toPagination(regionPage);

        return MemberRegionConverter.toGetRegionsByKeywordResponseDTO(regionSummaryList, pagination);
    }

}
