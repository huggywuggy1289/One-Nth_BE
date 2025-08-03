package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;


public interface MemberCommandService {

    //회원가입 로직 구현
    MemberResponseDTO.SignupResultDTO signupMember(MemberRequestDTO.SignupDTO request);

    //일반로그인 로직 구현
    MemberResponseDTO.LoginResultDTO loginMember(MemberRequestDTO.LoginRequestDTO request);

    //비밀번호 재설정 로직 구현
    MemberResponseDTO.PasswordResetResultDTO resetPassword(MemberRequestDTO.ResetPasswordRequestDTO request);

    //스크랩한 글 등록
    MemberResponseDTO.AddScrapOrLikeResponseDTO addScrap(Long memberId, Long postId);

    //스크랩한 글 취소
    MemberResponseDTO.CancelScrapOrLikeResponseDTO cancelScrap(Long memberId, Long postId);

    //공감한 글 등록
    MemberResponseDTO.AddScrapOrLikeResponseDTO addLike(Long memberId, Long postId);

    //공감한 글 취소
    MemberResponseDTO.CancelScrapOrLikeResponseDTO cancelLike(Long memberId, Long postId);

    //회원탈퇴 구현(soft delete)
    void withdrawMember(Long memberId);
}
