package com.onenth.OneNth.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListResponseDTO {
    private Long postId;           // 내부 식별용
    private String title;          // 게시글 제목
    private String contentPreview; // 게시글 내용 일부
    private int commentCount;      // 댓글 수
    private int likeCount;         // 공감 수
    private int viewCount;         // 조회 수
    private boolean scrapStatus;   // 현재 로그인한 사용자의 스크랩 여부
    private String createdAt;
}
