package com.onenth.OneNth.domain.post.dto;

import com.onenth.OneNth.domain.post.entity.PostComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostCommentResponseDTO {

    private Long id;
    private String content;
    private String nickname;
    private Long memberId;
    private LocalDateTime createdAt;


    public static PostCommentResponseDTO fromEntity(PostComment comment) {
        return PostCommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .memberId(comment.getMember().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
