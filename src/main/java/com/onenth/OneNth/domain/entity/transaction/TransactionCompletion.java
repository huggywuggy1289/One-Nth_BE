package com.onenth.OneNth.domain.entity.transaction;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction_completion")
public class TransactionCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_info_id")
    private Long id; // 거래완료폼 ID

    @Column(name = "trade_info_id2", nullable = false)
    private Long confirmationId; // 거래확정폼 ID

    @Column(name = "trade_date", nullable = false)
    private LocalDateTime tradeDate; // 거래일시

    @Column(name = "trade_price", nullable = false)
    private Integer tradePrice; // 거래금액

    @Column(name = "trade_type", length = 10, nullable = false)
    private String tradeType; // 거래방식

    @Column(name = "tracking_number", length = 50)
    private String trackingNumber; // 운송장번호

    @Column(name = "is_accepted", nullable = false)
    private Boolean isAccepted; // 수락여부

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId; // 거래 ID
}

