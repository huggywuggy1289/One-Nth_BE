package com.onenth.OneNth.domain.member.settings.alert.generalAlert.service;

import com.onenth.OneNth.domain.member.settings.alert.generalAlert.dto.GeneralAlertResponseDTO;

public interface GeneralAlertQueryService {

    GeneralAlertResponseDTO.GetAllAlertSettingsResponseDTO getAllAlertSettings(
            Long userId
    );

}
