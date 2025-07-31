package com.onenth.OneNth.global.auth.resolver;

import com.onenth.OneNth.global.auth.annotation.AuthUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class) &&
                parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // 또는 throw new UnauthenticatedException();
        }

        // principal로부터 userId 꺼내기 (너가 토큰에 어떻게 담았는지에 따라 다름)
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return Long.valueOf(userDetails.getUsername()); // username 대신 id를 넣었다면
        }

        // 혹시 email이 아니라 id 자체를 직접 토큰 subject에 넣었다면
        if (principal instanceof org.springframework.security.core.userdetails.User user) {
            return Long.valueOf(user.getUsername()); // string -> long 변환
        }

        return null;
    }
}
