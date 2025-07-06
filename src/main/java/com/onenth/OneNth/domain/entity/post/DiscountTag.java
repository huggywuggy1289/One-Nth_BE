package com.onenth.OneNth.domain.entity.post;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "discount_tag")
public class DiscountTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 매핑ID (PK)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일시

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id2", nullable = false)
    private Post post; // 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id3", nullable = false)
    private Tag tag; // 태그
}
