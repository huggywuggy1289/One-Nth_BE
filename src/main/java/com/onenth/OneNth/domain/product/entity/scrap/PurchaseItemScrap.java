package com.onenth.OneNth.domain.product.entity.scrap;

import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.onenth.OneNth.domain.member.entity.Member;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "purchase_item_scrap")
public class PurchaseItemScrap {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_item_id", nullable = false)
    private PurchaseItem purchaseItem;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

