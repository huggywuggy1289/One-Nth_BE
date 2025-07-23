package com.onenth.OneNth.domain.alert.service;

import com.onenth.OneNth.domain.alert.converter.AlertConverter;
import com.onenth.OneNth.domain.alert.dto.AlertRequestDTO;
import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;
import com.onenth.OneNth.domain.alert.entity.ProductKeywordAlert;
import com.onenth.OneNth.domain.alert.entity.RegionKeywordAlert;
import com.onenth.OneNth.domain.alert.repository.ProductKeywordAlertRepository;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertCommandServiceImpl implements AlertCommandService {

    private final RegionKeywordAlertRepository regionKeywordAlertRepository;
    private final ProductKeywordAlertRepository productKeywordAlertRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;

    @Override
    public AlertResponseDTO.AddKeywordAlertResponseDTO addRegionKeyword(Long userId, Long regionId) {

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

        return AlertConverter.toAddKeywordAlertResponseDTO(regionKeywordAlertRepository.save(regionKeywordAlert));

    }

    @Override
    public AlertResponseDTO.SetKeywordAlertStatusResponseDTO setRegionAlertStatus(
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

        return AlertConverter.toSetKeywordAlertStatusResponseDTO(regionKeywordAlertRepository.save(regionKeywordAlert));
    }

    @Override
    public AlertResponseDTO.AddKeywordAlertResponseDTO addProductKeyword(Long userId, AlertRequestDTO.AddKeywordAlertRequestDTO request) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (productKeywordAlertRepository.countByMember(member) >= 5) {
            throw new GeneralException(ErrorStatus.PRODUCT_KEYWORD_LIMIT_EXCEEDED);
        }

        if (productKeywordAlertRepository.existsByMemberAndKeyword(member, request.getKeyword())) {
            throw new GeneralException(ErrorStatus.PRODUCT_KEYWORD_ALREADY_EXISTS);
        }

        ProductKeywordAlert productKeywordAlert = AlertConverter.toProductKeywordAlert(member, request.getKeyword());

        return AlertConverter.toAddKeywordAlertResponseDTO(productKeywordAlertRepository.save(productKeywordAlert));

    }

    @Override
    public AlertResponseDTO.SetKeywordAlertStatusResponseDTO setProductKeywordAlertStatus(
            Long userId,
            Long productKeywordAlertId,
            AlertRequestDTO.SetKeywordAlertStatusRequestDTO request
    ) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        ProductKeywordAlert productKeywordAlert = productKeywordAlertRepository.findByIdAndMember(productKeywordAlertId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PRODUCT_KEYWORD_NOT_FOUND_OR_NOT_YOURS));

        if (request.getIsEnabled()) {
            productKeywordAlert.enable();
        } else {
            productKeywordAlert.disable();
        }

        return AlertConverter.toSetKeywordAlertStatusResponseDTO(productKeywordAlertRepository.save(productKeywordAlert));
    }

    @Override
    public AlertResponseDTO.AlertListResponseDTO updateKeywordAlertList(
            Long userId,
            AlertRequestDTO.UpdateKeywordAlertListRequestDTO request
    ) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Long> productKeywordIds = request.getProductKeywordIdList();
        List<Long> regionKeywordIds = request.getRegionKeywordIdList();

        deleteUnselectedKeywordAlerts(member, productKeywordIds, regionKeywordIds);

        List<ProductKeywordAlert> productKeywordAlertList = findProductAlertsByIds(productKeywordIds, member);
        List<RegionKeywordAlert> regionKeywordAlertList = findRegionAlertsByIds(regionKeywordIds, member);

        List<Object> mergedAlerts = mergeAndSortAlerts(productKeywordAlertList, regionKeywordAlertList);

        List<AlertResponseDTO.AlertSummary> alertSummaryList = mergedAlerts.stream()
                .map(alert -> AlertConverter.toAlertSummary(alert))
                .collect(Collectors.toList());

        return AlertConverter.toAlertListResponseDTO(alertSummaryList);
    }

    public void deleteUnselectedKeywordAlerts(Member member, List<Long> productAlertIds, List<Long> regionAlertIds) {
        List<ProductKeywordAlert> existingProductKeywordAlerts = productKeywordAlertRepository.findAllByMember(member);
        List<RegionKeywordAlert> existingRegionAlerts = regionKeywordAlertRepository.findAllByMember(member);

        List<ProductKeywordAlert> productKeywordAlertsToDelete = existingProductKeywordAlerts.stream()
                .filter(alert -> !productAlertIds.contains(alert.getId()))
                .collect(Collectors.toList());

        List<RegionKeywordAlert> regionAlertsToDelete = existingRegionAlerts.stream()
                .filter(alert -> !regionAlertIds.contains(alert.getId()))
                .collect(Collectors.toList());

        productKeywordAlertRepository.deleteAll(productKeywordAlertsToDelete);
        regionKeywordAlertRepository.deleteAll(regionAlertsToDelete);
    }

    private List<ProductKeywordAlert> findProductAlertsByIds(List<Long> productAlertIds, Member member) {
        return productAlertIds.stream().map(
                id -> {
                    return productKeywordAlertRepository.findByIdAndMember(id, member)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.PRODUCT_KEYWORD_NOT_FOUND_OR_NOT_YOURS));
                }).collect(Collectors.toList());
    }

    private List<RegionKeywordAlert> findRegionAlertsByIds(List<Long> regionAlertIds, Member member) {
        return regionAlertIds.stream().map(
                id -> {
                    return regionKeywordAlertRepository.findByIdAndMember(id, member)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.REGION_KEYWORD_NOT_FOUND_OR_NOT_YOURS));
                }).collect(Collectors.toList());
    }

    private List<Object> mergeAndSortAlerts(List<ProductKeywordAlert> productKeywordAlertList, List<RegionKeywordAlert> regionKeywordAlertList) {
        List<Object> mergedAlerts = new ArrayList<>();
        mergedAlerts.addAll(productKeywordAlertList);
        mergedAlerts.addAll(regionKeywordAlertList);

        mergedAlerts.sort(Comparator.comparing(alert ->
                        (alert instanceof ProductKeywordAlert)
                                ? ((ProductKeywordAlert) alert).getCreatedAt()
                                : ((RegionKeywordAlert) alert).getCreatedAt(),
                Comparator.reverseOrder())
        );

        return mergedAlerts;
    }
}
