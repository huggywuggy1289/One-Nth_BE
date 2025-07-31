package com.onenth.OneNth.domain.product.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.entity.enums.TradeMethod;
import com.onenth.OneNth.domain.product.entity.review.SharingReview;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharingItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemCategory itemCategory;

    private LocalDate expirationDate;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseMethod purchaseMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "sharingItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "sharingItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharingReview> sharingReviews = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToMany
    @JoinTable(
            name = "sharing_item_tag",
            joinColumns = @JoinColumn(name = "sharing_item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    // +
    @Column(length = 300, nullable = true)
    private String sharingLocation;

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}