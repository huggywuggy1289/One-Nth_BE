package com.onenth.OneNth.domain.alert.controller;

import com.onenth.OneNth.domain.alert.dto.AlertRequestDTO;
import com.onenth.OneNth.domain.alert.dto.AlertResponseDTO;
import com.onenth.OneNth.domain.alert.service.AlertCommandService;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsRequestDTO;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.ErrorReasonDTO;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-settings")
public class AlertController {

    private final AlertCommandService alertCommandService;

    @Operation(
            summary = "지역 키워드 알림 등록 API",
            description = "사용자 설정 중 지역 키워드를 알림으로 등록하는 API입니다. 응답으로 regionKeywordAlertId와 regionKeywordName, 알림 활성화 여부(enabled)을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 키워드 알림 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION_ALERT001", description = "등록 가능 지역 알림은 최대 3개입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION_ALERT002", description = "이미 알림으로 등록한 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PostMapping("/keyword-alerts/regions/{regionId}")
    public ApiResponse<AlertResponseDTO.AddRegionAlertResponseDTO> addRegionKeywordAlert(
            @Parameter(hidden=true) @AuthUser Long userId,
            @PathVariable Long regionId
    ) {
        return ApiResponse.onSuccess(alertCommandService.addRegionKeyword(userId, regionId));
    }

    @Operation(
            summary = "지역 키워드 알림 on/off API",
            description = "사용자 설정 중 지역 키워드 알림을 끄거나 켜는 API입니다. 응답으로 regionKeywordAlertId와 regionKeywordName, 알림 활성화 여부(enabled)을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 키워드 알림 상태 업데이트 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION_ALERT003", description = "해당 지역 키워드 알림이 존재하지 않거나 접근 권한이 없습니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PatchMapping("/keyword-alerts/regions/{regionKeywordAlertId}")
    public ApiResponse<AlertResponseDTO.SetRegionAlertStatusResponseDTO> setRegionAlertStatus(
            @Parameter(hidden=true) @AuthUser Long userId,
            @PathVariable Long regionKeywordAlertId,
            @RequestBody AlertRequestDTO.SetRegionAlertStatusRequestDTO request
    ) {
        return ApiResponse.onSuccess(alertCommandService.setRegionAlertStatus(userId, regionKeywordAlertId, request));
    }

    @Operation(
            summary = "일반 키워드 알림 등록 API",
            description = "사용자 설정 중 일반 키워드를 알림으로 등록하는 API입니다. 응답으로 keywordAlertId와 keyword, 알림 활성화 여부(enabled)을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 키워드 알림 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "KEYWORD_ALERT001", description = "등록 가능 키워드 알림은 최대 5개입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "KEYWORD_ALERT002", description = "이미 알림으로 등록한 키워드입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PostMapping("/keyword-alerts")
    public ApiResponse<AlertResponseDTO.AddKeywordAlertResponseDTO> addKeywordAlert(
            @Parameter(hidden=true) @AuthUser Long userId,
            @Valid @RequestBody AlertRequestDTO.AddKeywordAlertRequestDTO request
    ) {
        return ApiResponse.onSuccess(alertCommandService.addKeyword(userId, request));
    }
}
