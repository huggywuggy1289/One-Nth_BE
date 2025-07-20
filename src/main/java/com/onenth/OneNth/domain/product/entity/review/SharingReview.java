package com.onenth.OneNth.domain.product.entity.review;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharingReview extends BaseEntity implements Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rate;

    @Column(length = 2048, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_item_id", nullable = false)
    private SharingItem sharingItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SharingReviewImage> reviewImages = new ArrayList<>();

    public void addReviewImage(SharingReviewImage image) {
        this.reviewImages.add(image);
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}