package com.onenth.OneNth.domain.product.entity.review;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
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
public class PurchaseReview extends BaseEntity implements Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rate;

    @Column(length = 2048, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_item_id", nullable = false)
    private PurchaseItem purchaseItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "purchaseReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseReviewImage> reviewImages = new ArrayList<>();

    public void addReviewImage(PurchaseReviewImage image) {
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

    @Override
    public List<ReviewImage> getReviewImages() {
        return (List<ReviewImage>) (List<?>) this.reviewImages;
    }

    @Override
    public Long getItemId() {
        return this.purchaseItem.getId();
    }
}