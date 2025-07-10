package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.converter.MemberConverter;
import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.entity.EmailVerificationCode;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.service.EmailVerificationService.EmailService;
import com.onenth.OneNth.domain.member.service.EmailVerificationService.EmailVerificationService;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.configuration.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public MemberResponseDTO.SignupResultDTO signupMember(MemberRequestDTO.SignupDTO request) {

        //이메일 인증 여부 우선 확인
        if (!emailVerificationService.isVerified(request.getEmail())) {
            throw new RuntimeException("이메일 인증을 먼저 완료해주세요");
        }
        // 1. 지역명으로 Region 조회
        Region region = regionRepository.findByRegionName(request.getRegionName())
                .orElseThrow(() -> new RuntimeException("해당 지역이 존재하지 않습니다."));

        // 2. Member + MemberRegion 생성
        Member member = MemberConverter.toMember(request, region);

        //3. 비밀번호 암호화
        member.encodePassword(passwordEncoder.encode(request.getPassword()));

        //4. 회원 저장
        return MemberConverter.toSignupResultDTO(memberRepository.save(member));
    }

    @Override
    public MemberResponseDTO.LoginResultDTO loginMember(MemberRequestDTO.LoginRequestDTO request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 이메일과 일치하는 사용자가 없습니다."));

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member.getEmail(), null,
                Collections.emptyList()
        );

        String accessToken = jwtTokenProvider.generateToken(authentication);

        return MemberConverter.toLoginResultDTO(
                member.getId(),
                accessToken
        );
    }
}
