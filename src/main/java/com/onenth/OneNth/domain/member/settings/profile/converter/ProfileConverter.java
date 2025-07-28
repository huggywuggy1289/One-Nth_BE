package com.onenth.OneNth.domain.member.settings.profile.converter;

import com.onenth.OneNth.domain.member.settings.profile.dto.ProfileResponseDTO;
import org.springframework.context.annotation.Profile;

public class ProfileConverter {

    public static ProfileResponseDTO.UpdateProfileImageResponseDTO toUpdateProfileImageResponseDTO(String imageUrl) {
        return ProfileResponseDTO.UpdateProfileImageResponseDTO.builder()
                .profileImageUrl(imageUrl).build();
    }
}
