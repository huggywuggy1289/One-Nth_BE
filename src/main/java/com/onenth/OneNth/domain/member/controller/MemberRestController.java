package com.onenth.OneNth.domain.member.controller;

import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.service.memberService.MemberCommandService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/signup")
    public ApiResponse<MemberResponseDTO.SignupResultDTO> signup(@RequestBody @Valid MemberRequestDTO.SignupDTO request) {
        return ApiResponse.onSuccess(memberCommandService.signupMember(request));
    }
}
