package com.onenth.OneNth.domain.entity.user;

import com.onenth.OneNth.domain.enums.LoginType; // 분리
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "login_option")
public class LoginOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 로그인ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user; // 회원ID(FK) FK -> User.id

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType; // 로그인종류

    @Column(name = "login_key", length = 30, nullable = false)
    private String loginKey; // 로그인 키

    @Column(name = "password", length = 30, nullable = false)
    private String password; // 비밀번호

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일

    @Column(name = "user_id2", nullable = false)
    private Long userId2; //회원 ID
}
