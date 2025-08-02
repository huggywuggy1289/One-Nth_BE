package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.post.dto.PostCommentResponseDTO;

import java.util.List;

public interface PostCommentQueryService {
    List<PostCommentResponseDTO> getCommentsByPostId(Long postId);
}
