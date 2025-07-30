package com.onenth.OneNth.domain.member.settings.controller;

import com.onenth.OneNth.domain.member.settings.dto.UserSettingsRequestDTO;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;
import com.onenth.OneNth.domain.member.settings.service.UserSettingsCommandService;
import com.onenth.OneNth.domain.member.settings.service.UserSettingsQueryService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.ErrorReasonDTO;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 설정 - 우리동네 관련 API", description = "우리동네 등록, 인증, 삭제, 목록 조회, 메인으로 등록  API")
@RestController
@RequestMapping("/api/user-settings/regions")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsCommandService userSettingsCommandService;
    private final UserSettingsQueryService userSettingsQueryService;

    @Operation(
            summary = "우리 동네 추가 API",
            description = "사용자 설정 중 우리 동네를 등록하는 API입니다. 응답으로 regionId와 regionName, main(해당 지역이 메인인지 여부)을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER_REGION001", description = "등록 가능 지역은 최대 3개입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER_REGION002", description = "이미 등록한 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PostMapping("")
    public ApiResponse<UserSettingsResponseDTO.AddMyRegionResponseDTO> addMyRegion(
            @Parameter(hidden=true) @AuthUser Long userId,
            @Valid @RequestBody UserSettingsRequestDTO.AddMyRegionRequestDTO request
    ) {
        return ApiResponse.onSuccess(userSettingsCommandService.addMyRegion(userId, request));
    }

    @Operation(
            summary = "우리 동네 지역 삭제 API",
            description = "사용자 설정 중 우리 동네로 등록한 지역을 삭제하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER_REGION003", description = "해당 사용자가 등록하지 않은 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @DeleteMapping("/{regionId}")
    public ApiResponse<Void> deleteMyRegion(
            @Parameter(hidden=true) @AuthUser Long userId,
            @PathVariable Long regionId
    ) {
        userSettingsCommandService.deleteMyRegion(userId, regionId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(
            summary = "등록한 우리동네 지역 목록 조회 API",
            description = "사용자 설정 중 우리 동네로 등록한 지역 목록을 조회하는 API입니다. 응답으로 regionId와 regionName, main(해당 지역이 메인인지 여부)를 포함한 객체 list를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 우리 동네 지역 조회 성공", content = @Content(
                    schema = @Schema(implementation = UserSettingsResponseDTO.MyRegionListResponseDTO.class)
            )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("")
    public ApiResponse<UserSettingsResponseDTO.MyRegionListResponseDTO> getMyRegions(
            @Parameter(hidden=true) @AuthUser Long userId
    ) {
        return ApiResponse.onSuccess(userSettingsQueryService.getMyRegions(userId));
    }

    @Operation(
            summary = "특정 동네 메인으로 등록 API",
            description = "사용자 설정 중 특정 동네를 메인에 표시할 지역으로 등록하는 API입니다. 응답으로 regionId와 regionName, main(메인 여부)을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER_REGION003", description = "해당 사용자가 등록하지 않은 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PatchMapping("/{regionId}")
    public ApiResponse<UserSettingsResponseDTO.UpdateMainRegionResponseDTO> updateMainRegion(
            @Parameter(hidden=true) @AuthUser Long userId,
            @PathVariable Long regionId
    ) {
        return ApiResponse.onSuccess(userSettingsCommandService.updateMainRegion(userId, regionId));
    }
}
