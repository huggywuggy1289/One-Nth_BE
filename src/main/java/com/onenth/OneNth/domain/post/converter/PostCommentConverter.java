package com.onenth.OneNth.domain.post.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.post.dto.PostCommentResponseDTO;
import com.onenth.OneNth.domain.post.dto.PostCommentSaveRequestDTO;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.PostComment;

public class PostCommentConverter {

    public static PostComment toEntity(PostCommentSaveRequestDTO dto, Post post, Member member) {
        return PostComment.builder()
                .post(post)
                .member(member)
                .content(dto.getContent())
                .build();
    }

    public static PostCommentResponseDTO toResponseDTO(PostComment entity) {
        return PostCommentResponseDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .nickname(entity.getMember().getNickname())
                .memberId(entity.getMember().getId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
