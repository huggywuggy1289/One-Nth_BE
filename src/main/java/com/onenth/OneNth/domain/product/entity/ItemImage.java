package com.onenth.OneNth.domain.product.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_item_id", nullable = true)
    private SharingItem sharingItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_item_id", nullable = true)
    private PurchaseItem purchaseItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ItemType itemType;

    @PrePersist
    @PreUpdate
    private void validateItem() {
        boolean hasSharing = sharingItem != null;
        boolean hasPurchase = purchaseItem != null;

        if (hasSharing == hasPurchase) {
            throw new IllegalStateException("이미지는 나눔 아이템 또는 구매 아이템 중 하나에만 연결되어야 합니다.");
        }
    }
}