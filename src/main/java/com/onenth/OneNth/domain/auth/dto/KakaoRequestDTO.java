package com.onenth.OneNth.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class KakaoRequestDTO {

    @Getter
    @Setter
    public static class KakaoLoginRequestDTO {
        @NotBlank(message = "인가코드는 필수입니다.")
        private String code; // 카카오 인가 코드
    }

    @Getter
    @Setter
    public static class KakaoSignupRequestDTO {
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @NotBlank(message = "소셜 id는 필수입니다.")
        private String socialId;

        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        @NotBlank(message = "닉네임은 필수입니다.")
        private String nickname;

        @NotBlank(message = "지역명은 필수입니다.")
        private String regionName;

        @NotNull(message = "마케팅 동의 여부는 필수입니다.")
        private Boolean marketingAgree;
    }
}
