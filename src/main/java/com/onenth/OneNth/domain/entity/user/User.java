package com.onenth.OneNth.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.onenth.OneNth.domain.entity.region.Region;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 회원 ID
    private Long id;

    // 사용자이름(본명)
    private String username;

    // 비밀번호
    private String password;

    // 핸드폰 번호
    @Column(unique = true)
    private String phoneNumber;

    // 통신사
    private String carrier;

    // 생년월일
    private LocalDate birthDate;

    // 닉네임
    private String nickname;


    // 거주지역 FK Region.region
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region", referencedColumnName = "region")
    private Region region;

    // 이메일
    private String email;

    // 정보&이벤트 수신 동의 여부
    private Boolean marketingAgree;

    // 생성일
    private LocalDateTime createdAt;

    // 수정일
    private LocalDateTime updatedAt;
}
