package com.onenth.OneNth.domain.member.controller;

import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.service.EmailVerificationService.EmailVerificationService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "E-mail 인증 관련 API",
        description = "E-mail 인증코드 발송/ 인증코드 검증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email-auth")
public class EmailVerificationRestController {

    private final EmailVerificationService emailVerificationService;

    /**
     * 회원가입을 위해 이메일로 인증 코드 발송 API
     */
    @Operation(
            summary = "회원가입을 위한 이메일 인증 코드 발송 API",
            description = "이메일로 인증 코드를 발송합니다. 이메일 인증에 사용하세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/request-code")
    public ApiResponse<String> requestCode(@RequestBody MemberRequestDTO.EmailCodeRequestDTO request) {
        emailVerificationService.sendCode(request.getEmail());
        return ApiResponse.onSuccess("이메일로 인증번호가 전송되었습니다.");
    }

    /**
     * 인증코드를 입력후 유효성 검증 API
     */
    @Operation(
            summary = "회원가입을 위한 인증코드 검증 API",
            description = "이메일로 발송된 인증코드를 검증합니다. 올바른 코드인지 확인하세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/verify-code")
    public ApiResponse<String> verifyCode(@RequestBody MemberRequestDTO.VerifyCodeRequestDTO request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getCode());
        return ApiResponse.onSuccess("이메일 인증이 완료되었습니다.");
    }

    /**
     *  비밀번호 찾기 용 이메일 인증번호 발송 API
     */
    @Operation(
            summary = "비밀번호 찾기 용 이메일 인증번호 발송 API",
            description = "비밀번호를 재설정하기 위해서 이메일 인증을 해야합니다. 인증번호를 요청해주세요"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/password/request-code")
    public ApiResponse<String> sendPasswordFindCode(@RequestBody MemberRequestDTO.PasswordFindRequestDTO request) {
        emailVerificationService.sendCodeForPasswordReset(request);
        return ApiResponse.onSuccess("이메일로 인증번호가 전송되었습니다.");
    }

    /**
     *  비밀번호 찾기 용 이메일 인증번호 검증 API
     */
    @Operation(
            summary = "비밀번호 찾기 용 이메일 인증번호 검증 API",
            description = "비밀번호를 재설정하기 위해서 발급받은 인증번호를 검증해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/password/code-verify")
    public ApiResponse<String> verifyPasswordFindCode(@RequestBody MemberRequestDTO.VerifyCodeRequestDTO request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getCode());
        return ApiResponse.onSuccess("이메일 인증이 완료되었습니다. 비밀번호를 재설정 해주세요");
    }
}
