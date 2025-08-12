package com.onenth.OneNth.domain.alert.converter;

import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;
import com.onenth.OneNth.domain.alert.entity.Alert;

public class AlertConverter {

    public static AlertResponseDTO.DealNotificationResponseDTO toDealNotificationResponseDTO(Alert alert) {
        return AlertResponseDTO.DealNotificationResponseDTO.builder()
                .alertType(alert.getAlertType())
                .itemType(alert.getItemType())
                .contentId(alert.getContentId())
                .message(alert.getMessage())
                .readStatus(alert.isRead())
                .build();
    }

    public static AlertResponseDTO.PostNotificationResponseDTO toPostNotificationResponseDTO(Alert alert) {
        return AlertResponseDTO.PostNotificationResponseDTO.builder()
                .alertType(alert.getAlertType())
                .contentId(alert.getContentId())
                .message(alert.getMessage())
                .readStatus(alert.isRead())
                .build();
    }
}
