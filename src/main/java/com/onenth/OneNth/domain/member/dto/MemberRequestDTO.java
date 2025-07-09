package com.onenth.OneNth.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

public class MemberRequestDTO {

    // 회원가입 요청 dto
    @Getter
    public static class SignupDTO{
        @NotBlank
        String name;

        @Email
        @NotBlank
        String email;

        @NotBlank
        String password;

        @NotBlank
        String nickname;

        @NotBlank
        String regionName;

        @NotNull
        private LocalDate birthday;

    }
}
