package com.onenth.OneNth.domain.entity.auth;

import com.onenth.OneNth.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "auth_code")
public class AuthCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 인증번호ID

    @Column(name = "phone_number")
    private String phoneNumber; // 인증 대상 휴대폰 번호

    @Column
    private String code; // 발송된 인증번호

    @Column(name = "is_verified")
    private Boolean isVerified; // 인증 성공 여부

    @Column(name = "sent_at")
    private LocalDateTime sentAt; // 인증번호 발송 시각

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt; // 인증 처리 시각

    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // 인증번호 만료 시각

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 인증 요청한 회원
}