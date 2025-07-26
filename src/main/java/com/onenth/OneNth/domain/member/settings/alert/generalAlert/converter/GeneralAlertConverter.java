package com.onenth.OneNth.domain.member.settings.alert.generalAlert.converter;

import com.onenth.OneNth.domain.alert.entity.AlertType;
import com.onenth.OneNth.domain.member.entity.MemberAlertSetting;
import com.onenth.OneNth.domain.member.settings.alert.generalAlert.dto.GeneralAlertResponseDTO;

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
}
