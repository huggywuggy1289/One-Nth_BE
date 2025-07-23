package com.onenth.OneNth.domain.alert.converter;

import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;
import com.onenth.OneNth.domain.alert.entity.ProductKeywordAlert;
import com.onenth.OneNth.domain.alert.entity.RegionKeywordAlert;
import com.onenth.OneNth.domain.alert.entity.enums.KeywordAlertType;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.region.entity.Region;

import java.util.List;

public class AlertConverter {

    public static RegionKeywordAlert toRegionKeywordAlert(Member member, Region region) {
        return RegionKeywordAlert.builder()
                .member(member)
                .regionKeyword(region)
                .enabled(true)
                .build();
    }

    public static AlertResponseDTO.AddKeywordAlertResponseDTO toAddKeywordAlertResponseDTO(RegionKeywordAlert regionKeywordAlert) {
        return AlertResponseDTO.AddKeywordAlertResponseDTO.builder()
                .keywordAlertType(KeywordAlertType.REGION)
                .keywordAlertId(regionKeywordAlert.getId())
                .keyword(regionKeywordAlert.getRegionKeyword().getRegionName())
                .enabled(regionKeywordAlert.isEnabled())
                .build();
    }

    public static AlertResponseDTO.SetKeywordAlertStatusResponseDTO toSetKeywordAlertStatusResponseDTO(RegionKeywordAlert regionKeywordAlert) {
        return AlertResponseDTO.SetKeywordAlertStatusResponseDTO.builder()
                .keywordAlertType(KeywordAlertType.REGION)
                .keywordAlertId(regionKeywordAlert.getId())
                .keyword(regionKeywordAlert.getRegionKeyword().getRegionName())
                .enabled(regionKeywordAlert.isEnabled())
                .build();
    }

    public static ProductKeywordAlert toProductKeywordAlert(Member member, String keyword) {
        return ProductKeywordAlert.builder()
                .member(member)
                .keyword(keyword)
                .enabled(true)
                .build();
    }

    public static AlertResponseDTO.AddKeywordAlertResponseDTO toAddKeywordAlertResponseDTO(ProductKeywordAlert productKeywordAlert) {
        return AlertResponseDTO.AddKeywordAlertResponseDTO.builder()
                .keywordAlertType(KeywordAlertType.PRODUCT)
                .keywordAlertId(productKeywordAlert.getId())
                .keyword(productKeywordAlert.getKeyword())
                .enabled(productKeywordAlert.isEnabled())
                .build();
    }

    public static AlertResponseDTO.SetKeywordAlertStatusResponseDTO toSetKeywordAlertStatusResponseDTO(ProductKeywordAlert productKeywordAlert) {
        return AlertResponseDTO.SetKeywordAlertStatusResponseDTO.builder()
                .keywordAlertType(KeywordAlertType.PRODUCT)
                .keywordAlertId(productKeywordAlert.getId())
                .keyword(productKeywordAlert.getKeyword())
                .enabled(productKeywordAlert.isEnabled())
                .build();
    }

    public static AlertResponseDTO.AlertListResponseDTO toAlertListResponseDTO(List<AlertResponseDTO.AlertSummary> alertSummaryList) {
        return AlertResponseDTO.AlertListResponseDTO.builder()
                .alertSummaryList(alertSummaryList)
                .build();
    }

    public static AlertResponseDTO.AlertSummary toAlertSummary(Object alert) {
        if (alert instanceof ProductKeywordAlert) {
            return AlertConverter.toAlertSummaryFromProductKeywordAlert((ProductKeywordAlert) alert);
        } else {
            return AlertConverter.toAlertSummaryFromRegionKeywordAlert((RegionKeywordAlert) alert);
        }
    }

    public static AlertResponseDTO.AlertSummary toAlertSummaryFromProductKeywordAlert(ProductKeywordAlert productKeywordAlert) {
        return AlertResponseDTO.AlertSummary.builder()
                .keywordAlertType(KeywordAlertType.PRODUCT)
                .alertId(productKeywordAlert.getId())
                .keyword(productKeywordAlert.getKeyword())
                .enabled(productKeywordAlert.isEnabled())
                .build();
    }

    public static AlertResponseDTO.AlertSummary toAlertSummaryFromRegionKeywordAlert(RegionKeywordAlert regionKeywordAlert) {
        return AlertResponseDTO.AlertSummary.builder()
                .keywordAlertType(KeywordAlertType.REGION)
                .alertId(regionKeywordAlert.getId())
                .keyword(regionKeywordAlert.getRegionKeyword().getRegionName())
                .enabled(regionKeywordAlert.isEnabled())
                .build();
    }
}
