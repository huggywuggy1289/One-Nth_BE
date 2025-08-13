package com.onenth.OneNth.domain.alert.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ItemType itemType = null;

    @Column(nullable = false)
    private Long contentId;

    @Column(length = 500, nullable = false)
    private String message;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(nullable = false)
    @Builder.Default
    @Setter
    private boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;
}