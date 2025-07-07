package com.onenth.OneNth.domain.transaction.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCancellationReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_transaction_id", nullable = false)
    private CancelledTransaction cancelledTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancellation_reason_id", nullable = false)
    private CancellationReason cancellationReason;
}