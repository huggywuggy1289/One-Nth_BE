package com.onenth.OneNth.domain.entity.user;

import com.onenth.OneNth.domain.entity.region.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "keyword_alert_setting")
public class KeywordAlertSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region; // 지역 아이디

    @Column(length = 20, nullable = false)
    private String keyword; // 키워드

    @Column(nullable = false)
    private boolean enabled = true; // 알람수신여부
}
