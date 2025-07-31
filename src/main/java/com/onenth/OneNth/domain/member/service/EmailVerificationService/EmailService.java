package com.onenth.OneNth.domain.member.service.EmailVerificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) {
        String subject = "[N분의1] 이메일 인증번호입니다.";
        String content = "**인증번호는 다음과 같습니다.**\n" + code + "\n **코드 유효시간은 5분입니다.**";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }


}
