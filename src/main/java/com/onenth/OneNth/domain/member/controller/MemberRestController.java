package com.onenth.OneNth.domain.member.controller;

import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.service.memberService.MemberCommandService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 계정 관련 API",
        description = "일반 로그인/회원가입 계정 찾기 등 계정관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberCommandService memberCommandService;

    /**
     * 일반 회원가입 API 구현
     */
    @Operation(
            summary = "일반 회원가입 API",
            description = "일반 회원가입을 진행합니다. 이메일 인증 후 회원 정보를 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/signup")
    public ApiResponse<MemberResponseDTO.SignupResultDTO> signup(@RequestBody @Valid MemberRequestDTO.SignupDTO request) {
        return ApiResponse.onSuccess(memberCommandService.signupMember(request));
    }

    /**
     * 일반 로그인 API 구현
     */
    @Operation(
            summary = "일반 로그인 API",
            description = "일반 로그인을 진행합니다. 응답으로 JWT 토큰이 발급됩니다. 헤더에 담아서 사용하세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/login")
    public ApiResponse<MemberResponseDTO.LoginResultDTO> login(@RequestBody @Valid MemberRequestDTO.LoginRequestDTO request) {
        return ApiResponse.onSuccess(memberCommandService.loginMember(request));
    }

}
