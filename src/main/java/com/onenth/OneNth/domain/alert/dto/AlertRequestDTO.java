package com.onenth.OneNth.domain.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AlertRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FcmTokenRequestDTO{
        private String fcmToken;
    }
}
