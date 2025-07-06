package com.onenth.OneNth.domain.entity.chat;

import com.onenth.OneNth.domain.enums.ChatRoomType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id; // 채팅방 ID

    @Column(name = "chat_room_name", length = 500, nullable = false)
    private String name; // 채팅방 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_room_type", length = 10, nullable = false)
    private ChatRoomType type; // 채팅방 유형 (SALE, PURCHASE)

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed; // 거래 확정 여부
}
