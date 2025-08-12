package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;

public interface MemberQueryService {
    // 마이페이지 - 내가 스크랩한 글 조회하기 (size 개씩 페이징)
    MemberResponseDTO.PostListDTO getScrappedPosts(Long memberId, Integer page, Integer size);

    // 마이페이지 - 내가 좋아요한 글 조회하기 (size 개씩 페이징)
    MemberResponseDTO.PostListDTO getLikedPosts(Long memberId, Integer page, Integer size);

    // 마이페이지 - 내가 쓴 게시글 조회하기 (size 개씩 페이징)
    MemberResponseDTO.PostListDTO getMyPosts(Long memberId, Integer page, Integer size);

    // ID 기반 사용자 프로필 정보 조회
    MemberResponseDTO.MemberProfilePreviewDTO getMemberProfilePreview(Long memberId);

    // 사용자가 쓴 거래글 조회
    MemberResponseDTO.ItemPreviewListDTO getMyAllItems(Long memberId, Integer page, Integer size);

    // 사용자가 스크랩한 거래글 조회
    MemberResponseDTO.ItemPreviewListDTO getMyScrappedItems(Long memberId, Integer page, Integer size);
}
