package com.onenth.OneNth.domain.entity.product;

import com.onenth.OneNth.domain.enums.SharingStatus;
import com.onenth.OneNth.domain.enums.TradeMethod;
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
@Table(name = "sharing_item")
public class SharingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 함께 나눠요 상품 ID

    @Column(length = 50, nullable = false)
    private String name; // 상품명

    @Column(name = "image_url")
    private String imageUrl; // 이미지 URL

    @Column(nullable = false)
    private Integer quantity; // 판매 수량

    @Column(name = "sales_price", nullable = false)
    private Integer salesPrice; // 판매 가격

    @Column(length = 20, nullable = false)
    private String category; // 상품 카테고리

    @Column(name = "expiration_date")
    private LocalDate expirationDate; // 소비 기한

    @Column(name = "is_verified")
    private Boolean isVerified; // 거래 가능 상품 여부

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_mtehod", length = 20, nullable = false)
    private TradeMethod tradeMethod; // 거래 방식 (DIRECT, DELIVERY)

    @Column(name = "trade_location", length = 50)
    private String tradeLocation; // 거래 장소

    @Column(name = "storage_way", length = 100)
    private String storageWay; // 보관 방법

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region", nullable = false)
    private Region region; // 지역 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SharingStatus status; // 상태 (DEFAULT, IN_PROGRESS, COMPLETED)
}
