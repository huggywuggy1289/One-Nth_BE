package com.onenth.OneNth.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KakaoResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KakaoLoginResponseDTO {
        private String access_token;
        private String refresh_token;
        private String email;
        private String name;
        private String serialId;
        private Boolean isNew; //true 면 회원가입 로직 필요
    }

}
