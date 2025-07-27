package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.post.dto.PostDetailResponseDTO;
import com.onenth.OneNth.domain.post.dto.PostListResponseDTO;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PostQueryService {

    // 게시글 상세 조회
    public PostDetailResponseDTO getPostDetail(Long postId, Member member);

    // 게시글 목록 조회 (regionName 또는 keyword로)
    Page<PostListResponseDTO> getPostList(
            PostType postType,
            String regionName,   // 지역 필터링용 (nullable)
            String keyword,      // 키워드 검색용 (nullable)
            Pageable pageable    // 페이징
    );
}
