package com.onenth.OneNth.domain.member.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberNotificationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean chatNotifications = false; // 채팅알림 수신여부

    @Column(nullable = false)
    private boolean reviewNotifications = false; // 리뷰알림 수신여부

    @Column(nullable = false)
    private boolean scrapNotifications = false; // 스크랩알림 수신여부

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;
}