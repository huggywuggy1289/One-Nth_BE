package com.onenth.OneNth.domain.member.service.EmailVerificationService;

import com.onenth.OneNth.domain.member.entity.EmailVerificationCode;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.emailVerificationTokenRepository.EmailVerificationCodeRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationCodeRepository codeRepository;
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    public void sendCode(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException("올바르지 않은 이메일 형식입니다.");
        }

        // 이메일 중복 검증 (회원가입 DB와 연결 필요)
        if (memberRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        String code = generateCode();

        EmailVerificationCode emailVerificationCode = EmailVerificationCode.builder()
                .email(email)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .isVerified(false)
                .build();

        codeRepository.save(emailVerificationCode);
        emailService.sendVerificationEmail(email, code);
    }

    public void verifyCode(String email, String code) {
        //검증할 타겟 코드 불러오기
        EmailVerificationCode targetCode = codeRepository.findTopByEmailOrderByExpiresAtDesc(email)
                .orElseThrow(() -> new RuntimeException("인증 요청이 없습니다."));

        //유효시간이 남아있는지 검증
        if (targetCode.isExpired()) { throw new RuntimeException("인증번호가 만료되었습니다."); }

        //입력한 코드가 타겟 코드와 같은지 검증
        if (!targetCode.getCode().equals(code)) { throw new RuntimeException("인증번호가 일치하지 않습니다."); }

        //검증됨 으로 타겟코드 변경
        targetCode.markVerified();

        //변경사항 저장
        codeRepository.save(targetCode);
    }

    public String generateCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    public boolean isVerified(String email) {
        return codeRepository.findTopByEmailOrderByExpiresAtDesc(email)
                .map(EmailVerificationCode::isVerified)
                .orElse(false);
    }
}