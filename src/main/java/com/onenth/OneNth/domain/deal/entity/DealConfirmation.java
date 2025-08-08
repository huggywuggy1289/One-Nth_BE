package com.onenth.OneNth.domain.deal.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.deal.entity.enums.TradeType;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealConfirmation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate tradeDate;

    private Integer tradePrice;

    private Integer originalPrice;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType tradeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Member buyer;

    @OneToOne(mappedBy = "dealConfirmation", cascade = CascadeType.ALL, orphanRemoval = true)
    private DealCompletion dealCompletion;
}