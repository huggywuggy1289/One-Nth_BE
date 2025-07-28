package com.onenth.OneNth.domain.member.settings.profile.dto;

import lombok.Builder;
import lombok.Getter;

public class ProfileResponseDTO {

    @Getter
    @Builder
    public static class UpdateProfileImageResponseDTO {
        private String profileImageUrl;
    }

}
