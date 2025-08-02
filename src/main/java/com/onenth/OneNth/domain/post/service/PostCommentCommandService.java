package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.post.dto.PostCommentSaveRequestDTO;
import com.onenth.OneNth.domain.post.entity.PostComment;

public interface PostCommentCommandService {
    Long saveComment(Long postId, Long memberId, PostCommentSaveRequestDTO requestDto);
    void deleteComment(Long commentId, Long memberId);
    PostComment findById(Long commentId);
}
