package com.onenth.OneNth.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

public class KakaoRequestDTO {

    @Getter
    @Setter
    public static class KakaoLoginRequestDTO {
        private String code; // 카카오 인가 코드
    }

    @Getter
    @Setter
    public static class KakaoSignupRequestDTO {
        private String email;
        private String socialId;
        private String name;
        private String nickname;
        private String regionName;
        private Boolean marketingAgree;
    }
}
