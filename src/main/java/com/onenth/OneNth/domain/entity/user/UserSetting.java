package com.onenth.OneNth.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_setting")
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 아이디

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 아이디

    @Column(name = "recive_chat_notifications", nullable = false)
    private boolean reciveChatNotifications; // 채팅알림 수신여부

    @Column(name = "recive_review_notifications", nullable = false)
    private boolean reciveReviewNotifications; // 리뷰알림 수신여부

    @Column(name = "recive_scrap_notifications", nullable = false)
    private boolean reciveScrapNotifications; // 스크랩알림 수신여부
}
