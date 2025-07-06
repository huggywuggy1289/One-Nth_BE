package com.onenth.OneNth.domain.entity.transaction;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cancellation_reason")
public class CancellationReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancellation_reason_id")
    private Long id; // 거래취소사유 ID

    @Column(name = "reason_text", length = 20)
    private String reasonText; // 거래취소사유 내용
}

