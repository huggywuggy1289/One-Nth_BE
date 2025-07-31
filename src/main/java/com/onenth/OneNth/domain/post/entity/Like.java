package com.onenth.OneNth.domain.post.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

        @Entity
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Table(name = "`like`")
        public class Like extends BaseEntity {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "user_id", nullable = false)
            private Member member;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "post_id", nullable = false)
            private Post post;
        }