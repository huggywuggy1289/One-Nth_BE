package com.onenth.OneNth.domain.transaction.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.transaction.entity.enums.TradeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionConfirmation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

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

    @OneToOne(mappedBy = "transactionConfirmation", cascade = CascadeType.ALL, orphanRemoval = true)
    private TransactionCompletion transactionCompletion;
}