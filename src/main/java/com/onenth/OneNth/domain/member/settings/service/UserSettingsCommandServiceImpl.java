package com.onenth.OneNth.domain.member.settings.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.settings.converter.UserSettingsConverter;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsRequestDTO;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSettingsCommandServiceImpl implements UserSettingsCommandService {

    private final MemberRepository memberRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final RegionRepository regionRepository;

    @Override
    public UserSettingsResponseDTO.AddMyRegionResponseDTO addMyRegion(Long userId, UserSettingsRequestDTO.AddMyRegionRequestDTO request) {

        // feature/#1 병합 후 회원 연동
        Member member = memberRepository.findByIdWithRegions(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));


        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_NOT_FOUND));

        if (memberRegionRepository.countByMember(member) >= 3) {
            throw new GeneralException(ErrorStatus.REGION_LIMIT_EXCEEDED);
        }

        if (memberRegionRepository.existsByMemberAndRegion(member, region)) {
            throw new GeneralException(ErrorStatus.REGION_ALREADY_EXISTS);
        }

        MemberRegion memberRegion = UserSettingsConverter.toMemberRegion(member, region);

        memberRegionRepository.save(memberRegion);

        return UserSettingsConverter.toAddMyRegionResponseDTO(memberRegion.getRegion());
    }


}
