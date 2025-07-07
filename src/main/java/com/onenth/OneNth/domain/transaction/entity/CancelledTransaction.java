package com.onenth.OneNth.domain.transaction.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelledTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long transactionId;

    @OneToMany(mappedBy = "cancelledTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionCancellationReason> transactionCancellationReasonList = new ArrayList<>();
}