package com.onenth.OneNth.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo {
    private String email;
    // 이름으로 사용
    private String nickname;
    // 카카오 사용자 고유 아이디
    private String id;
}
