package com.onenth.OneNth.domain.alert.service;

import com.onenth.OneNth.domain.alert.converter.AlertConverter;
import com.onenth.OneNth.domain.alert.dto.AlertRequestDTO;
import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;
import com.onenth.OneNth.domain.alert.entity.RegionKeywordAlert;
import com.onenth.OneNth.domain.alert.repository.RegionKeywordAlertRepository;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
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
public class AlertCommandServiceImpl implements AlertCommandService {

    private final RegionKeywordAlertRepository regionKeywordAlertRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;

    @Override
    public AlertResponseDTO.AddRegionAlertResponseDTO addRegionKeyword(Long userId, Long regionId) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_NOT_FOUND));

        if (regionKeywordAlertRepository.countByMember(member) >= 3) {
            throw new GeneralException(ErrorStatus.REGION_KEYWORD_LIMIT_EXCEEDED);
        }

        if (regionKeywordAlertRepository.existsByMemberAndRegionKeyword(member, region)) {
            throw new GeneralException(ErrorStatus.REGION_KEYWORD_ALREADY_EXISTS);
        }

        RegionKeywordAlert regionKeywordAlert = AlertConverter.toRegionKeywordAlert(member, region);

        return AlertConverter.toAddAlertResponseDTO(regionKeywordAlertRepository.save(regionKeywordAlert));

    }

    @Override
    public AlertResponseDTO.SetRegionAlertStatusResponseDTO setRegionAlertStatus(
            Long userId,
            Long regionKeywordAlertId,
            AlertRequestDTO.SetRegionAlertStatusRequestDTO request) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        RegionKeywordAlert regionKeywordAlert = regionKeywordAlertRepository.findByIdAndMember(regionKeywordAlertId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_KEYWORD_NOT_FOUND_OR_NOT_YOURS));

        if (request.getIsEnabled()) {
            regionKeywordAlert.enable();
        } else {
            regionKeywordAlert.disable();
        }

        return AlertConverter.toSetRegionAlertStatusResponseDTO(regionKeywordAlertRepository.save(regionKeywordAlert));
    }

}
