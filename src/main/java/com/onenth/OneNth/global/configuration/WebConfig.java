package com.onenth.OneNth.global.configuration;

import com.onenth.OneNth.global.auth.resolver.AuthUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthUserArgumentResolver authUserArgumentResolver;

    public WebConfig(AuthUserArgumentResolver resolver) {
        this.authUserArgumentResolver = resolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserArgumentResolver);
    }
}