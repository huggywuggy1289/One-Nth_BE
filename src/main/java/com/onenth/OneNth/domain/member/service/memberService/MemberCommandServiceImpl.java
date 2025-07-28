package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.converter.MemberConverter;
import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.entity.EmailVerificationCode;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberAlertSetting;
import com.onenth.OneNth.domain.member.repository.memberAlertSettingRepository.MemberAlertSettingRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.service.EmailVerificationService.EmailService;
import com.onenth.OneNth.domain.member.service.EmailVerificationService.EmailVerificationService;
import com.onenth.OneNth.domain.post.entity.Like;
import com.onenth.OneNth.domain.post.entity.Scrap;
import com.onenth.OneNth.domain.post.repository.likeRepository.LikeRepository;
import com.onenth.OneNth.domain.post.repository.scrapRepository.ScrapRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.configuration.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.onenth.OneNth.domain.post.entity.QScrap.scrap;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ScrapRepository scrapRepository;
    private final LikeRepository likeRepository;
    private final MemberAlertSettingRepository memberAlertSettingRepository;
    @Override
    public MemberResponseDTO.SignupResultDTO signupMember(MemberRequestDTO.SignupDTO request) {

        //이메일 인증 여부 우선 확인
        if (!emailVerificationService.isVerified(request.getEmail(), "signup")) {
            throw new RuntimeException("이메일 인증을 먼저 완료해주세요");
        }

        // 1. Password 와 confirmPassword 값 일치 확인
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Password 와 ConfirmPassword 의 값이 일치하지 않습니다.");
        }

        // 2. 지역명으로 Region 조회
        Region region = regionRepository.findByRegionName(request.getRegionName())
                .orElseThrow(() -> new RuntimeException("해당 지역이 존재하지 않습니다."));

        // 3. Member + MemberRegion 생성
        Member member = MemberConverter.toMember(request, region);

        // 4. 비밀번호 암호화
        member.encodePassword(passwordEncoder.encode(request.getPassword()));

        // 5. 회원 저장
        Member savedMember = memberRepository.save(member);

        // 알림설정 생성 (기본값 ON)
        MemberAlertSetting alertSetting = MemberAlertSetting.builder()
                .member(savedMember)
                .chatAlerts(true)
                .scrapAlerts(true)
                .reviewAlerts(true)
                .build();
        memberAlertSettingRepository.save(alertSetting);

        return MemberConverter.toSignupResultDTO(savedMember);
    }

    @Override
    public MemberResponseDTO.LoginResultDTO loginMember(MemberRequestDTO.LoginRequestDTO request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 이메일과 일치하는 사용자가 없습니다."));

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member.getId().toString(),
                null,
                Collections.emptyList()
        );

        String accessToken = jwtTokenProvider.generateToken(authentication);

        return MemberConverter.toLoginResultDTO(
                member.getId(),
                accessToken
        );
    }

    @Override
    public MemberResponseDTO.PasswordResetResultDTO resetPassword(MemberRequestDTO.ResetPasswordRequestDTO request) {
        //이메일 인증 여부 확인
        if (!emailVerificationService.isVerified(request.getEmail(), "reset_password")) {
            throw new RuntimeException("이메일 인증이 완료되지 않았습니다.");
        }

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        // 새 비밀번호 암호화 저장
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);

        return MemberResponseDTO.PasswordResetResultDTO.builder().isSuccess(true).build();
    }

    @Override
    public MemberResponseDTO.CancelScrapOrLikeResponseDTO cancelScrap(Long memberId, Long postId) {
        Scrap scrap = scrapRepository.findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new RuntimeException("해당 멤버가 스크랩한 글이 없습니다."));

        scrapRepository.delete(scrap);

        return MemberResponseDTO.CancelScrapOrLikeResponseDTO.builder()
                .isSuccess(true)
                .build();
    }

    @Override
    public MemberResponseDTO.CancelScrapOrLikeResponseDTO cancelLike(Long memberId, Long postId) {
        Like like = likeRepository.findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new RuntimeException("해당 멤버가 공감한 글이 없습니다."));

        likeRepository.delete(like);

        return MemberResponseDTO.CancelScrapOrLikeResponseDTO.builder()
                .isSuccess(true)
                .build();
    }
}
