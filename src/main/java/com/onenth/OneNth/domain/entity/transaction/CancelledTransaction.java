package com.onenth.OneNth.domain.entity.transaction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cancelled_transaction")
public class CancelledTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancelled_transaction_id")
    private Long id; // 거래취소 ID

    @Column(name = "transaction_id2", nullable = false)
    private Long transactionId; // 거래 ID
}
