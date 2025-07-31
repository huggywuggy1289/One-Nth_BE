package com.onenth.OneNth.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * controller 외 전역에서 jwt 토큰에 정보를 사용할 수 있는 utility 클래스
 */
public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인 되지 않았습니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return Long.valueOf(user.getUsername());
        }

        throw new RuntimeException("잘못된 사용자 정보입니다.");
    }
}
