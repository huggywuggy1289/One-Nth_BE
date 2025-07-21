package com.onenth.OneNth.domain.alert.converter;

import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;
import com.onenth.OneNth.domain.alert.entity.Alert;
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

    public static AlertResponseDTO.AddRegionAlertResponseDTO toAddAlertResponseDTO(RegionKeywordAlert regionKeywordAlert) {
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
}
