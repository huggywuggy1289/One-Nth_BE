package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface MemberCommandService {

    //회원가입 로직 구현
    MemberResponseDTO.SignupResultDTO signupMember(MemberRequestDTO.SignupDTO request);

    //일반로그인 로직 구현
    MemberResponseDTO.LoginResultDTO loginMember(MemberRequestDTO.LoginRequestDTO request);
}
