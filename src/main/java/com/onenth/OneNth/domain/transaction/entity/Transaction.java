package com.onenth.OneNth.domain.transaction.entity;

import com.onenth.OneNth.domain.chat.entity.ChatRoom;
import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_item_id")
    private PurchaseItem purchaseItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_item_id")
    private SharingItem sharingItem;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private TransactionConfirmation transactionConfirmation;

    @PrePersist
    @PreUpdate
    private void validateItem() {
        boolean hasPurchase = purchaseItem != null;
        boolean hasSharing = sharingItem != null;

        if (hasPurchase == hasSharing) {
            throw new IllegalStateException("구매 아이템과 나눔 아이템 중 하나만 존재해야 합니다.");
        }
    }
}