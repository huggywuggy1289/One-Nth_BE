package com.onenth.OneNth.domain.member.settings.alert.generalAlert.converter;

import com.onenth.OneNth.domain.alert.entity.AlertType;
import com.onenth.OneNth.domain.member.entity.MemberAlertSetting;
import com.onenth.OneNth.domain.member.entity.ProductKeywordAlert;
import com.onenth.OneNth.domain.member.entity.RegionKeywordAlert;
import com.onenth.OneNth.domain.member.entity.enums.KeywordAlertType;
import com.onenth.OneNth.domain.member.settings.alert.generalAlert.dto.GeneralAlertResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;

import java.util.List;

public class GeneralAlertConverter {

    public static GeneralAlertResponseDTO.SetScrapAlertStatusResponseDTO toSetScrapAlertStatusResponseDTO(MemberAlertSetting memberAlertSetting) {
        return GeneralAlertResponseDTO.SetScrapAlertStatusResponseDTO.builder()
                .alertType(AlertType.SCRAP)
                .enabled(memberAlertSetting.isScrapAlerts())
                .build();
    }

    public static GeneralAlertResponseDTO.SetReviewAlertStatusResponseDTO toSetReviewAlertStatusResponseDTO(MemberAlertSetting memberAlertSetting) {
        return GeneralAlertResponseDTO.SetReviewAlertStatusResponseDTO.builder()
                .alertType(AlertType.REVIEW)
                .enabled(memberAlertSetting.isReviewAlerts())
                .build();
    }

    public static GeneralAlertResponseDTO.SetChatAlertStatusResponseDTO toSetChatAlertStatusResponseDTO(MemberAlertSetting memberAlertSetting) {
        return GeneralAlertResponseDTO.SetChatAlertStatusResponseDTO.builder()
                .alertType(AlertType.CHAT)
                .enabled(memberAlertSetting.isChatAlerts())
                .build();
    }

    public static GeneralAlertResponseDTO.GetAllAlertSettingsResponseDTO toGetAllAlertSettingsResponseDTO(
            GeneralAlertResponseDTO.GeneralAlertSummary scrapAlertSummary,
            GeneralAlertResponseDTO.GeneralAlertSummary reviewAlertSummary,
            List<GeneralAlertResponseDTO.KeywordAlertSummary> keywordAlertSummaryList
    ) {
        return GeneralAlertResponseDTO.GetAllAlertSettingsResponseDTO.builder()
                .scrapAlertSummary(scrapAlertSummary)
                .reviewAlertSummary(reviewAlertSummary)
                .keywordAlertSummaryList(keywordAlertSummaryList)
                .build();
    }

    public static GeneralAlertResponseDTO.GeneralAlertSummary toGeneralAlertSummary(AlertType alertType, MemberAlertSetting memberAlertSetting) {

        if (alertType == AlertType.SCRAP) {
            return GeneralAlertResponseDTO.GeneralAlertSummary.builder()
                    .alertType(alertType)
                    .enabled(memberAlertSetting.isScrapAlerts())
                    .build();
        } else if (alertType == AlertType.REVIEW) {
            return GeneralAlertResponseDTO.GeneralAlertSummary.builder()
                    .alertType(alertType)
                    .enabled(memberAlertSetting.isReviewAlerts())
                    .build();
        } else {
            throw new GeneralException(ErrorStatus.UNEXPECTED_ALERT_TYPE);
        }
    }

    public static GeneralAlertResponseDTO.KeywordAlertSummary toKeywordAlertSummary(Object keywordAlert) {
        if (keywordAlert instanceof ProductKeywordAlert) {
            return GeneralAlertResponseDTO.KeywordAlertSummary.builder()
                    .keywordAlertType(KeywordAlertType.PRODUCT)
                    .keywordAlertId(((ProductKeywordAlert) keywordAlert).getId())
                    .keyword(((ProductKeywordAlert) keywordAlert).getKeyword())
                    .enabled(((ProductKeywordAlert) keywordAlert).isEnabled())
                    .build();
        } else {
            return GeneralAlertResponseDTO.KeywordAlertSummary.builder()
                    .keywordAlertType(KeywordAlertType.REGION)
                    .keywordAlertId(((RegionKeywordAlert) keywordAlert).getId())
                    .keyword(((RegionKeywordAlert) keywordAlert).getRegionKeyword().getRegionName())
                    .enabled(((RegionKeywordAlert) keywordAlert).isEnabled())
                    .build();
        }
    }

}
