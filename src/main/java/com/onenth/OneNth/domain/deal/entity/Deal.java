package com.onenth.OneNth.domain.deal.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_item_id")
    private PurchaseItem purchaseItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_item_id")
    private SharingItem sharingItem;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_confirmation_id", nullable = false)
    private DealConfirmation dealConfirmation;

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