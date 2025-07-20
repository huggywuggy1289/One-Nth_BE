package com.onenth.OneNth.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostSaveResponseDTO {
    private Long postId;
    private LocalDateTime createdAt;
}
