package com.onenth.OneNth.domain.alert.controller;

import com.onenth.OneNth.domain.alert.dto.FcmRequestDTO;
import com.onenth.OneNth.domain.alert.service.FcmService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Tag(name = "FCM 관련 API")
public class FcmController {

    private final FcmService FCMService;

    @Operation(
            summary = "FCM 토큰 등록 API",
            description = "사용자의 FCM 토큰을 등록하거나 업데이트하는 API입니다. " +
                    "로그인된 사용자의 기기에서 전달된 FCM 토큰을 서버에 저장합니다."
    )
    @PostMapping("/token")
    public ApiResponse<String> registerFcmToken(
            @AuthUser Long memberId,
            @RequestBody FcmRequestDTO.FcmTokenRequestDTO request) {
        FCMService.registerFcmToken(memberId, request);
        return ApiResponse.onSuccess("토큰 갱신이 완료되었습니다");
    }

    @Operation(
            summary = "FCM 토큰 삭제 API",
            description = "사용자가 더 이상 알림을 받지 않으려는 특정 기기의 FCM 토큰을 서버에서 삭제합니다. " +
                    "주로 로그아웃, 앱 삭제, 토큰 교체 시 호출되어 불필요한 알림 전송을 방지합니다."
    )
    @DeleteMapping("/token")
    public ApiResponse<String> deleteFcmToken(
            @AuthUser Long memberId,
            @RequestBody FcmRequestDTO.FcmTokenRequestDTO request
    ) {
        FCMService.deleteFcmToken(memberId, request);
        return ApiResponse.onSuccess("토큰 삭제가 완료되었습니다");
    }


    @Operation(
            summary = "푸시 알림 테스트 발송",
            description = "사용자 계정에 등록된 FCM 토큰으로 테스트 푸시 알림을 하나 발송합니다."
    )
    @PostMapping("/test")
    public ApiResponse<String> sendTestNotification(
            @AuthUser Long memberId
    ) {
        FCMService.testNotification(memberId);
        return ApiResponse.onSuccess("푸시 알림 테스트 완료");
    }
}