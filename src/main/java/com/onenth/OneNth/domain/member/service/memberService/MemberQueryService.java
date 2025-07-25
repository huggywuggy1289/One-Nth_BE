package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;

public interface MemberQueryService {
    // 마이페이지 - 내가 스크랩한 글 조회하기 (size 개씩 페이징)
    MemberResponseDTO.PostListDTO getScrappedPosts(Long memberId, Integer page, Integer size);

    // 마이페이지 - 내가 좋아요한 글 조회하기 (size 개씩 페이징)
    MemberResponseDTO.PostListDTO getLikedPosts(Long memberId, Integer page, Integer size);

}
