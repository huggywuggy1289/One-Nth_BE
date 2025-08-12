package com.onenth.OneNth.domain.alert.controller;

import com.onenth.OneNth.domain.alert.dto.AlertRequestDTO;
import com.onenth.OneNth.domain.alert.service.AlertService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Tag(name = "FCM 관련 API")
public class AlertController {

    private final AlertService alertService;

    @Operation(
            summary = "FCM 토큰 등록 API",
            description = "사용자의 FCM 토큰을 등록하거나 업데이트하는 API입니다. " +
                    "로그인된 사용자의 기기에서 전달된 FCM 토큰을 서버에 저장합니다."
    )
    @PostMapping("/token")
    public ApiResponse<String> registerFcmToken(
            @AuthUser Long memberId,
            @RequestBody AlertRequestDTO.FcmTokenRequestDTO request) {
        alertService.registerFcmToken(memberId, request);
        return ApiResponse.onSuccess("토큰 갱신이 완료되었습니다");
    }

    @Operation(
            summary = "푸시 알림 테스트 발송",
            description = "사용자 계정에 등록된 FCM 토큰으로 테스트 푸시 알림을 하나 발송합니다."
    )
    @PostMapping("/test")
    public ApiResponse<String> sendTestNotification(
            @AuthUser Long memberId
    ) {
        alertService.testNotification(memberId);
        return ApiResponse.onSuccess("푸시 알림 테스트 완료");
    }
}