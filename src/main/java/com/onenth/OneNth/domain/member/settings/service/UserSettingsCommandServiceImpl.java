package com.onenth.OneNth.domain.member.settings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSettingsCommandServiceImpl implements UserSettingsCommandService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final MemberRepository memberRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final RegionRepository regionRepository;

    @Override
    public UserSettingsResponseDTO.AddMyRegionResponseDTO addMyRegion(Long userId, UserSettingsRequestDTO.AddMyRegionRequestDTO request) {

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

        return UserSettingsConverter.toAddMyRegionResponseDTO(memberRegion);
    }

    @Override
    public void deleteMyRegion(Long userId, Long regionId) {

        Member member = memberRepository.findByIdWithRegions(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_NOT_FOUND));

        MemberRegion memberRegion = memberRegionRepository.findByMemberAndRegion(member, region)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_REGION_NOT_FOUND));

        if (memberRegion.isMain()) {
            throw new GeneralException(ErrorStatus.CANNOT_DELETE_MAIN_REGION);
        }

        member.getMemberRegions().remove(memberRegion);
        memberRegionRepository.delete(memberRegion);

    }

    @Override
    public UserSettingsResponseDTO.UpdateMainRegionResponseDTO updateMainRegion(Long userId, Long regionId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Optional<MemberRegion> currentMainOpt = memberRegionRepository.findByMemberAndIsMain(member, true);
        currentMainOpt.ifPresent(currentMain -> currentMain.setMain(false));

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_NOT_FOUND));

        MemberRegion memberRegion = memberRegionRepository.findByMemberAndRegion(member, region)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_REGION_NOT_FOUND));

        memberRegion.setMain(true);

        return UserSettingsConverter.toUpdateMainRegionResponseDTO(memberRegion);
    }

    @Override
    public UserSettingsResponseDTO.VerifyMyRegionResponseDTO verifyMyRegion(Long userId, Long regionId, UserSettingsRequestDTO.VerifyMyRegionRequestDTO request){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_NOT_FOUND));

        MemberRegion memberRegion = memberRegionRepository.findByMemberAndRegion(member, region)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_REGION_NOT_FOUND));

        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json"
                + "?x=" + request.getLongitude() + "&y=" + request.getLatitude();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserSettingsResponseDTO.KakaoRegionResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserSettingsResponseDTO.KakaoRegionResponseDTO.class
        );

        UserSettingsResponseDTO.KakaoRegionResponseDTO responseBody = response.getBody();

        if (responseBody == null || responseBody.getDocuments() == null || responseBody.getDocuments().isEmpty()) {
            throw new GeneralException(ErrorStatus.EXTERNAL_API_ERROR);
        }

        UserSettingsResponseDTO.KakaoRegionResponseDTO.Document doc = responseBody
                .getDocuments()
                .get(0);

        String detectedRegionName = String.join(" ",
                doc.getRegion1DepthName(),
                doc.getRegion2DepthName(),
                doc.getRegion3DepthName());

        return UserSettingsResponseDTO.VerifyMyRegionResponseDTO.builder()
                .isVerified(detectedRegionName.equals(region.getRegionName()))
                .detectedRegionName(detectedRegionName)
                .requestedRegionName(region.getRegionName())
                .build();

    }

}
