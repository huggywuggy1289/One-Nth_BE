package com.onenth.OneNth.domain.alert.service;

import com.onenth.OneNth.domain.alert.dto.FcmRequestDTO;
import com.onenth.OneNth.domain.alert.entity.FcmToken;
import com.onenth.OneNth.domain.alert.fcm.FcmClient;
import com.onenth.OneNth.domain.alert.repository.FcmTokenRepository;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;
    private final FcmClient fcmClient;

    public void registerFcmToken(Long memberId, FcmRequestDTO.FcmTokenRequestDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        FcmToken fcmToken = fcmTokenRepository.findByMember(member)
                .orElse(null);

        if (fcmToken == null) {
            fcmToken = FcmToken.builder()
                    .member(member)
                    .fcmToken(request.getFcmToken())
                    .build();
            fcmTokenRepository.save(fcmToken);

        } else if (!fcmToken.getFcmToken().equals(request.getFcmToken())) {
            // 토큰 변경 시에만 업데이트
            fcmToken.setFcmToken(request.getFcmToken());
            fcmTokenRepository.save(fcmToken);
        }
    }

    public void deleteFcmToken(Long memberId, FcmRequestDTO.FcmTokenRequestDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        FcmToken fcmToken = fcmTokenRepository.findByMember(member)
                .orElse(null);

        if (fcmToken != null && fcmToken.getFcmToken().equals(request.getFcmToken())) {
            fcmTokenRepository.delete(fcmToken);
        }
    }

    public void testNotification(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        FcmToken fcmToken = fcmTokenRepository.findByMember(member)
                .orElse(null);

        String title = "Notification Test";
        String body = "Test";

        fcmClient.sendNotification(fcmToken.getFcmToken(), title, body);
    }
}
