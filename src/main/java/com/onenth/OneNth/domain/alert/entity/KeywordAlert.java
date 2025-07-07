package com.onenth.OneNth.domain.alert.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.region.entity.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordAlert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region regionKeyword;

    @Column(length = 20)
    private String keyword;

    @Column(nullable = false)
    private boolean enabled = true;

    @PrePersist
    @PreUpdate
    private void validateItem() {
        boolean hasPurchase = regionKeyword != null;
        boolean hasSharing = keyword != null;

        if (hasPurchase == hasSharing) {
            throw new IllegalStateException("키워드는 지역 키워드와 상품 키워드 중 하나만 존재해야 합니다.");
        }
    }
}