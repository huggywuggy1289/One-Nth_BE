package com.onenth.OneNth.domain.member.converter;

import com.onenth.OneNth.domain.auth.dto.KakaoRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.entity.enums.LoginType;
import com.onenth.OneNth.domain.region.entity.Region;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MemberConverter {

    //회원가입 요청 dto 를 Member 엔티티로 변환
    public static Member toMember(MemberRequestDTO.SignupDTO request, Region region) {
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .loginType(LoginType.NORMAL)
                .memberRegions(new ArrayList<>())
                .marketingAgree(request.getMarketingAgree())
                .build();

        //회원가입시에는 지역 한개만 매핑
        MemberRegion memberRegion = MemberRegion.builder()
                .member(member)
                .region(region)
                .build();

        member.getMemberRegions().add(memberRegion);

        return member;
    }

    //카카오 회원가입 요청 dto 를 Member 엔티티로 변환
    public static Member toMember(KakaoRequestDTO.KakaoSignupRequestDTO request, Region region) {
        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .socialId(request.getSocialId())
                .nickname(request.getNickname())
                .loginType(LoginType.KAKAO)
                .memberRegions(new ArrayList<>())
                .marketingAgree(request.getMarketingAgree())
                .build();

        //회원가입시에는 지역 한개만 매핑
        MemberRegion memberRegion = MemberRegion.of(member, region);
        member.getMemberRegions().add(memberRegion);

        return member;
    }

    // Member 엔티티를 회원가입 응답 dto 로 변환
    public static MemberResponseDTO.SignupResultDTO toSignupResultDTO(Member member) {
        return MemberResponseDTO.SignupResultDTO.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //로그인 결과 dto로 변환
    public static MemberResponseDTO.LoginResultDTO toLoginResultDTO(Long memberId, String accessToken) {
       return MemberResponseDTO.LoginResultDTO.builder()
               .memberId(memberId)
               .accessToken(accessToken)
               .build();
    }
}
