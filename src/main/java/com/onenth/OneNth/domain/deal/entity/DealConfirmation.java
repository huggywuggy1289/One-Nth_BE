package com.onenth.OneNth.domain.deal.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.deal.entity.enums.TradeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealConfirmation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal;

    @Column(nullable = false)
    private LocalDateTime tradeDate;

    private Integer tradePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType tradeType;

    @Column(length = 50)
    private String trackingNumber;

    @Column(nullable = false)
    private boolean isAccepted;

    @OneToOne(mappedBy = "dealConfirmation", cascade = CascadeType.ALL, orphanRemoval = true)
    private DealCompletion dealCompletion;
}