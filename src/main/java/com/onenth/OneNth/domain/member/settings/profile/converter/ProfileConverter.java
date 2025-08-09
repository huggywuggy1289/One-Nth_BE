package com.onenth.OneNth.domain.member.settings.profile.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.settings.profile.dto.ProfileResponseDTO;
import org.springframework.context.annotation.Profile;

import java.util.List;

public class ProfileConverter {

    public static ProfileResponseDTO.UpdateProfileImageResponseDTO toUpdateProfileImageResponseDTO(String imageUrl) {
        return ProfileResponseDTO.UpdateProfileImageResponseDTO.builder()
                .profileImageUrl(imageUrl).build();
    }

    public static ProfileResponseDTO.UpdateNicknameResponseDTO toUpdateProfileImageResponseDTO(Member member) {
        return ProfileResponseDTO.UpdateNicknameResponseDTO.builder()
                .nickname(member.getNickname())
                .build();
    }

    public static ProfileResponseDTO.GetMyProfileResponseDTO toGetMyProfileResponseDTO(Member member, List<String> verifiedRegionNames) {
        return ProfileResponseDTO.GetMyProfileResponseDTO.builder()
                .profileImageUrl(member.getProfileImageUrl())
                .nickname(member.getNickname())
                .verifiedRegionNames(verifiedRegionNames)
                .verifiedRegionNames(verifiedRegionNames)
                .build();
    }
}
