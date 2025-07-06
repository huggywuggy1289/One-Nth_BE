package com.onenth.OneNth.domain.entity.post;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 태그ID (PK)

    @Column(length = 10, nullable = false)
    private String name; // 태그명

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일시
}
