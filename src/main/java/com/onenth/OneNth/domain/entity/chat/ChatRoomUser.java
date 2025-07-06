package com.onenth.OneNth.domain.entity.chat;

import com.onenth.OneNth.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_room_user")
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 매핑 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom; // 채팅방

    @Column(name = "recive_notifications", nullable = false)
    private Boolean reciveNotifications; // 알림 수신 여부

    @Column(name = "last_read_at", nullable = false)
    private LocalDateTime lastReadAt; // 마지막 읽은 시각
}
