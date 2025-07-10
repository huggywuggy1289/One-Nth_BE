package com.onenth.OneNth.domain.member.controller;

import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.service.EmailVerificationService.EmailVerificationService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email-auth")
public class EmailVerificationRestController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/request-code")
    public ApiResponse<String> requestCode(@RequestBody MemberRequestDTO.EmailCodeRequestDTO request) {
        emailVerificationService.sendCode(request.getEmail());
        return ApiResponse.onSuccess("이메일로 인증번호가 전송되었습니다.");
    }

    @PostMapping("/verify-code")
    public ApiResponse<String> verifyCode(@RequestBody MemberRequestDTO.VerifyCodeRequestDTO request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getCode());
        return ApiResponse.onSuccess("이메일 인증이 완료되었습니다.");
    }
}
