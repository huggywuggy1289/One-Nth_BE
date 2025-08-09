package com.onenth.OneNth.domain.member.settings.profile.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.settings.profile.converter.ProfileConverter;
import com.onenth.OneNth.domain.member.settings.profile.dto.ProfileResponseDTO;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final MemberRepository memberRepository;
    private final MemberRegionRepository memberRegionRepository;

    public ProfileResponseDTO.GetMyProfileResponseDTO getMyProfile(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<String> verifiedRegionNames = memberRegionRepository.findByMemberAndIsVerifiedTrue(member)
                .stream()
                .map(memberRegion -> memberRegion.getRegion().getRegionName())
                .collect(Collectors.toList());

        return ProfileConverter.toGetMyProfileResponseDTO(member, verifiedRegionNames);
    }
}
