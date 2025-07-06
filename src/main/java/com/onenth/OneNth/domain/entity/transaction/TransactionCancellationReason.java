package com.onenth.OneNth.domain.entity.transaction;

import com.onenth.OneNth.domain.entity.transaction.CancellationReason;
import com.onenth.OneNth.domain.entity.transaction.CancelledTransaction;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction_cancellation_reason")
public class TransactionCancellationReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 거래취소사유연결 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_transaction_id", nullable = false)
    private CancelledTransaction cancelledTransaction; // 거래취소 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancellation_reason_id", nullable = false)
    private CancellationReason cancellationReason; // 거래취소사유 ID
}
