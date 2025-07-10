package com.onenth.OneNth.global.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("jwt.token")
public class JwtProperties {
    private String secretKey = "";
    private Expiration expiration;

    @Getter
    @Setter
    public static class Expiration {
        private Long access;
        // TODO: refreshToken
    }


}
