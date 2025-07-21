package com.onenth.OneNth.domain.alert.converter;

import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;
import com.onenth.OneNth.domain.alert.entity.KeywordAlert;
import com.onenth.OneNth.domain.alert.entity.RegionKeywordAlert;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.region.entity.Region;

public class AlertConverter {

    public static RegionKeywordAlert toRegionKeywordAlert(Member member, Region region) {
        return RegionKeywordAlert.builder()
                .member(member)
                .regionKeyword(region)
                .enabled(true)
                .build();
    }

    public static AlertResponseDTO.AddRegionAlertResponseDTO toAddRegionAlertResponseDTO(RegionKeywordAlert regionKeywordAlert) {
        return AlertResponseDTO.AddRegionAlertResponseDTO.builder()
                .regionKeywordAlertId(regionKeywordAlert.getId())
                .regionKeywordName(regionKeywordAlert.getRegionKeyword().getRegionName())
                .enabled(regionKeywordAlert.isEnabled())
                .build();
    }

    public static AlertResponseDTO.SetRegionAlertStatusResponseDTO toSetRegionAlertStatusResponseDTO(RegionKeywordAlert regionKeywordAlert) {
        return AlertResponseDTO.SetRegionAlertStatusResponseDTO.builder()
                .regionKeywordAlertId(regionKeywordAlert.getId())
                .regionKeywordName(regionKeywordAlert.getRegionKeyword().getRegionName())
                .enabled(regionKeywordAlert.isEnabled())
                .build();
    }

    public static KeywordAlert toKeywordAlert(Member member, String keyword) {
        return KeywordAlert.builder()
                .member(member)
                .keyword(keyword)
                .enabled(true)
                .build();
    }

    public static AlertResponseDTO.AddKeywordAlertResponseDTO toAddKeywordAlertResponse(KeywordAlert keywordAlert) {
        return AlertResponseDTO.AddKeywordAlertResponseDTO.builder()
                .keywordAlertId(keywordAlert.getId())
                .keyword(keywordAlert.getKeyword())
                .enabled(keywordAlert.isEnabled())
                .build();
    }

    public static AlertResponseDTO.SetKeywordAlertStatusResponseDTO toSetKeywordAlertStatusResponseDTO(KeywordAlert keywordAlert) {
        return AlertResponseDTO.SetKeywordAlertStatusResponseDTO.builder()
                .keywordAlertId(keywordAlert.getId())
                .keyword(keywordAlert.getKeyword())
                .enabled(keywordAlert.isEnabled())
                .build();
    }
}
