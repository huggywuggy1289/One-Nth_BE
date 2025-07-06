package com.onenth.OneNth.domain.entity.notification;

import com.onenth.OneNth.domain.enums.NotificationType;
import com.onenth.OneNth.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원id

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 20, nullable = false)
    private NotificationType notificationType; // 알림종류

    @Column(name = "target_id", nullable = false)
    private Long targetId; // 알림대상 아이디

    @Column(name = "target_type", length = 20, nullable = false)
    private String targetType; // 알림대상 유형

    @Lob
    @Column(nullable = false)
    private String message; // 알림 내용

    @Column(name = "is_read", nullable = false)
    private Boolean isRead; // 읽음 여부
}
