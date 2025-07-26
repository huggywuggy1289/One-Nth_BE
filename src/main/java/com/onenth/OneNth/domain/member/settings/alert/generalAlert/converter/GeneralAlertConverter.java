package com.onenth.OneNth.domain.member.settings.alert.generalAlert.converter;

import com.onenth.OneNth.domain.alert.entity.AlertType;
import com.onenth.OneNth.domain.member.entity.MemberAlertSetting;
import com.onenth.OneNth.domain.member.settings.alert.generalAlert.dto.GeneralAlertResponseDTO;

public class GeneralAlertConverter {

    public static GeneralAlertResponseDTO.SetScrapAlertStatusResponseDTO toSetScrapAlertStatusResponseDTO(AlertType alertType, MemberAlertSetting memberAlertSetting) {
        return GeneralAlertResponseDTO.SetScrapAlertStatusResponseDTO.builder()
                .alertType(alertType)
                .enabled(memberAlertSetting.isScrapAlerts())
                .build();
    }
}
