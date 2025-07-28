package com.onenth.OneNth.domain.member.settings.profile.controller;

import com.onenth.OneNth.domain.member.settings.alert.generalAlert.dto.GeneralAlertResponseDTO;
import com.onenth.OneNth.domain.member.settings.profile.dto.ProfileRequestDTO;
import com.onenth.OneNth.domain.member.settings.profile.dto.ProfileResponseDTO;
import com.onenth.OneNth.domain.member.settings.profile.service.ProfileCommandService;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "사용자 설정 - 계정 설정 관련 API", description = "프로필 이미지, 닉네임, 비밀번호 변경, 내 프로필 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-settings/profile")
public class ProfileController {

    private final ProfileCommandService profileCommandService;

    @Operation(
            summary =  "사용자 프로필 이미지 변경 API",
            description = "사용자 설정 중 프로필 이미지를 변경하는 API입니다. 응답으로 바뀐 프로필 이미지 url을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 사용자 프로필 이미지 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PROFILE001", description = "프로필 이미지가 비어 있거나 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProfileResponseDTO.UpdateProfileImageResponseDTO> updateProfileImage(
            @Parameter(hidden=true) @AuthUser Long userId,
            @RequestPart("image") MultipartFile image
    ) {
        return ApiResponse.onSuccess(profileCommandService.updateProfileImage(userId, image));
    }

    @Operation(
            summary =  "사용자 닉네임 변경 API",
            description = "사용자 설정 중 닉네임을 변경하는 API입니다. 응답으로 바뀐 닉네임을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 사용자 닉네임 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @PatchMapping("/nickname")
    public ApiResponse<ProfileResponseDTO.UpdateNicknameResponseDTO> updateNickname(
            @Parameter(hidden=true) @AuthUser Long userId,
            @Valid @RequestBody ProfileRequestDTO.UpdateNicknameRequestDTO request
    ) {
        return ApiResponse.onSuccess(profileCommandService.updateNickname(userId, request));
    }
}
