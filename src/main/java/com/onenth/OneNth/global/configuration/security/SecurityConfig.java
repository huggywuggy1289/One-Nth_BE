package com.onenth.OneNth.global.configuration.security;

import com.onenth.OneNth.global.configuration.security.jwt.JwtAuthenticationFilter;
import com.onenth.OneNth.global.configuration.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers("/",
                                        "/api/members/signup",
                                        "/api/members/login",
                                        "/api/email-auth/request-code",
                                        "/api/email-auth/code-verify",
                                        "/api/auth/kakao/login",
                                        "/api/auth/kakao/signup",
                                        "/api/auth/kakao/callback",
                                        "/api/email-auth/password/request-code",
                                        "/api/email-auth/password/code-verify",
                                        "/api/members/password/reset",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}