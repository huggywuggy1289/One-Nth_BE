package com.onenth.OneNth.domain.entity.post;

import com.onenth.OneNth.domain.entity.region.Region;
import com.onenth.OneNth.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 할인정보ID (PK)

    @Column(length = 200, nullable = false)
    private String title; // 글 제목

    @Lob
    private String content; // 글 내용

    @Column(nullable = false)
    private Integer views; // 조회수

    @Column(nullable = false)
    private Integer likes; // 좋아요 수

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id2", nullable = false)
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region2", nullable = false)
    private Region region; // 지역
}
