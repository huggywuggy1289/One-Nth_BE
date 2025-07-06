package com.onenth.OneNth.domain.entity.transaction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id; // 거래 ID

    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId; // 채팅방 ID

    @Column(name = "id2")
    private Long groupPurchaseItemId; // 같이 사요 상품 ID (nullable)

    @Column(name = "id3")
    private Long sharingItemId; // 함께 나눠요 상품 ID (nullable)
}
