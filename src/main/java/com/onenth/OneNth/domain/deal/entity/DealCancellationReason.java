package com.onenth.OneNth.domain.deal.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealCancellationReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_deal_id", nullable = false)
    private CancelledDeal cancelledDeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancellation_reason_id", nullable = false)
    private CancellationReason cancellationReason;
}