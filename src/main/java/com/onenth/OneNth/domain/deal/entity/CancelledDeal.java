package com.onenth.OneNth.domain.deal.entity;

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
public class CancelledDeal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long dealId;

    @OneToMany(mappedBy = "cancelledDeal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DealCancellationReason> dealCancellationReasonList = new ArrayList<>();
}