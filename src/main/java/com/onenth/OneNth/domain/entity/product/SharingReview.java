package com.onenth.OneNth.domain.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sharing_review")
public class SharingReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deal_review_id")
    private Long id; // 거래후기 ID

    @Column(name = "deal_review_rate", nullable = false, precision = 2, scale = 1)
    private Double rate; // 평점

    @Column(name = "deal_review_content", length = 2048, nullable = false)
    private String content; // 후기 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id2", nullable = false)
    private SharingItem sharingItem; // 함께 나눠요 상품 ID
}
