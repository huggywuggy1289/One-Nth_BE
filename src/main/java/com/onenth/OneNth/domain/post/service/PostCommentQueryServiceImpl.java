package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.post.dto.PostCommentResponseDTO;
import com.onenth.OneNth.domain.post.entity.PostComment;
import com.onenth.OneNth.domain.post.repository.commentRepository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentQueryServiceImpl implements PostCommentQueryService{

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PostCommentResponseDTO> getCommentsByPostId(Long postId) {
        List<PostComment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);

        return comments.stream()
                .map(PostCommentResponseDTO::fromEntity)
                .toList();
    }

}
