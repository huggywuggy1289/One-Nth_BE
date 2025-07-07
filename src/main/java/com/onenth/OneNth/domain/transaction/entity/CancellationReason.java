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
public class CancellationReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String reasonText;

    @OneToMany(mappedBy = "cancellationReason", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionCancellationReason> transactionCancellationReasonList = new ArrayList<>();
}