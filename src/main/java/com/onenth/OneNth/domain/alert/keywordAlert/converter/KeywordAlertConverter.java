package com.onenth.OneNth.domain.alert.keywordAlert.converter;

import com.onenth.OneNth.domain.alert.keywordAlert.dto.KeywordAlertResponseDTO;
import com.onenth.OneNth.domain.alert.keywordAlert.entity.ProductKeywordAlert;
import com.onenth.OneNth.domain.alert.keywordAlert.entity.RegionKeywordAlert;
import com.onenth.OneNth.domain.alert.keywordAlert.entity.enums.KeywordAlertType;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.region.entity.Region;

import java.util.List;

public class KeywordAlertConverter {

    public static RegionKeywordAlert toRegionKeywordAlert(Member member, Region region) {
        return RegionKeywordAlert.builder()
                .member(member)
                .regionKeyword(region)
                .enabled(true)
                .build();
    }

    public static KeywordAlertResponseDTO.AddKeywordAlertResponseDTO toAddKeywordAlertResponseDTO(RegionKeywordAlert regionKeywordAlert) {
        return KeywordAlertResponseDTO.AddKeywordAlertResponseDTO.builder()
                .keywordAlertType(KeywordAlertType.REGION)
                .keywordAlertId(regionKeywordAlert.getId())
                .keyword(regionKeywordAlert.getRegionKeyword().getRegionName())
                .enabled(regionKeywordAlert.isEnabled())
                .build();
    }

    public static KeywordAlertResponseDTO.SetKeywordAlertStatusResponseDTO toSetKeywordAlertStatusResponseDTO(RegionKeywordAlert regionKeywordAlert) {
        return KeywordAlertResponseDTO.SetKeywordAlertStatusResponseDTO.builder()
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

    public static KeywordAlertResponseDTO.AddKeywordAlertResponseDTO toAddKeywordAlertResponseDTO(ProductKeywordAlert productKeywordAlert) {
        return KeywordAlertResponseDTO.AddKeywordAlertResponseDTO.builder()
                .keywordAlertType(KeywordAlertType.PRODUCT)
                .keywordAlertId(productKeywordAlert.getId())
                .keyword(productKeywordAlert.getKeyword())
                .enabled(productKeywordAlert.isEnabled())
                .build();
    }

    public static KeywordAlertResponseDTO.SetKeywordAlertStatusResponseDTO toSetKeywordAlertStatusResponseDTO(ProductKeywordAlert productKeywordAlert) {
        return KeywordAlertResponseDTO.SetKeywordAlertStatusResponseDTO.builder()
                .keywordAlertType(KeywordAlertType.PRODUCT)
                .keywordAlertId(productKeywordAlert.getId())
                .keyword(productKeywordAlert.getKeyword())
                .enabled(productKeywordAlert.isEnabled())
                .build();
    }

    public static KeywordAlertResponseDTO.AlertListResponseDTO toAlertListResponseDTO(List<KeywordAlertResponseDTO.AlertSummary> alertSummaryList) {
        return KeywordAlertResponseDTO.AlertListResponseDTO.builder()
                .alertSummaryList(alertSummaryList)
                .build();
    }

    public static KeywordAlertResponseDTO.AlertSummary toAlertSummary(Object alert) {
        if (alert instanceof ProductKeywordAlert) {
            return KeywordAlertConverter.toAlertSummaryFromProductKeywordAlert((ProductKeywordAlert) alert);
        } else {
            return KeywordAlertConverter.toAlertSummaryFromRegionKeywordAlert((RegionKeywordAlert) alert);
        }
    }

    public static KeywordAlertResponseDTO.AlertSummary toAlertSummaryFromProductKeywordAlert(ProductKeywordAlert productKeywordAlert) {
        return KeywordAlertResponseDTO.AlertSummary.builder()
                .keywordAlertType(KeywordAlertType.PRODUCT)
                .alertId(productKeywordAlert.getId())
                .keyword(productKeywordAlert.getKeyword())
                .enabled(productKeywordAlert.isEnabled())
                .build();
    }

    public static KeywordAlertResponseDTO.AlertSummary toAlertSummaryFromRegionKeywordAlert(RegionKeywordAlert regionKeywordAlert) {
        return KeywordAlertResponseDTO.AlertSummary.builder()
                .keywordAlertType(KeywordAlertType.REGION)
                .alertId(regionKeywordAlert.getId())
                .keyword(regionKeywordAlert.getRegionKeyword().getRegionName())
                .enabled(regionKeywordAlert.isEnabled())
                .build();
    }
}
