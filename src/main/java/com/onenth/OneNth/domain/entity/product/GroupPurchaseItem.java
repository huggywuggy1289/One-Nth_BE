package com.onenth.OneNth.domain.entity.product;

import com.onenth.OneNth.domain.enums.GroupPurchaseStatus;
import com.onenth.OneNth.domain.enums.PurchaseMethod;
import com.onenth.OneNth.domain.entity.region.Region;
import com.onenth.OneNth.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "group_purchase_item")
public class GroupPurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 같이 사요 상품 ID

    @Column(length = 50, nullable = false)
    private String name; // 상품명

    @Column(name = "image_url")
    private String imageUrl; // 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_method", nullable = false)
    private PurchaseMethod purchaseMethod; // 구매 방식 (ONLINE, OFFLINE)

    @Column(length = 20, nullable = false)
    private String category; // 상품 카테고리

    @Column(name = "purchase_url", length = 255, nullable = false)
    private String purchaseUrl; // 상품 구매 링크

    @Column(name = "purchase_location", length = 20)
    private String purchaseLocation; // 상품 구매 위치

    @Column(name = "expiration_date")
    private LocalDate expirationDate; // 상품 소비 기한

    @Column(nullable = false)
    private Integer price; // 원가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region", nullable = false)
    private Region region; // 지역 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private GroupPurchaseStatus status; // 상태 (DEFAULT, IN_PROGRESS, COMPLETED)
}
