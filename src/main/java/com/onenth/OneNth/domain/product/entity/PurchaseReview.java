package com.onenth.OneNth.domain.product.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.onenth.OneNth.domain.member.entity.Member; //+

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rate;

    @Column(length = 2048, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_item_id", nullable = false)
    private PurchaseItem purchaseItem;

    // +
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}