package com.onenth.OneNth.domain.member.settings.block.controller;

import com.onenth.OneNth.domain.member.settings.alert.keywordAlert.dto.KeywordAlertResponseDTO;
import com.onenth.OneNth.domain.member.settings.block.service.BlockCommandService;
import com.onenth.OneNth.domain.member.settings.block.service.BlockQueryService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.ErrorReasonDTO;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 설정 - 차단 관련 API", description = "사용자 설정 중 차단 목록 조회, 차단 해제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-settings/blocks")
public class BlockController {

    private final BlockCommandService blockCommandService;

    @Operation(
            summary = "사용자 차단 해제 API",
            description = "사용자 설정 중 특정 사용자를 차단해제하는 API입니다. 응답 객체로 아무것도 반환하지 않습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 키워드 알림 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @DeleteMapping("/{blockedUserId}")
    public ApiResponse<Void> unblockUser(
            @Parameter(hidden=true) @AuthUser Long userId,
            @PathVariable Long blockedUserId
    ) {
        return ApiResponse.onSuccess(blockCommandService.unblockUser(userId, blockedUserId));
    }
}
