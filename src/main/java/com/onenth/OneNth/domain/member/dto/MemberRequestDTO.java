package com.onenth.OneNth.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

public class MemberRequestDTO {

    //이메일 인증 코드 요청 dto
    @Getter
    public static class EmailCodeRequestDTO {
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;
    }

    //인증 코드 검증 요청 dto
    @Getter
    public static class VerifyCodeRequestDTO {
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @NotBlank(message = "이메일 검증코드는 필수입니다.")
        private String code;
    }

    // 회원가입 요청 dto
    @Getter
    public static class SignupDTO{
        @NotBlank
        String name;

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "확인용 비밀번호는 필수입니다.")
        private String confirmPassword;

        @NotBlank(message = "닉네임은 필수입니다.")
        private String nickname;

        @NotBlank(message = "지역명은 필수입니다.")
        private String regionName;

//        @NotNull
//        private LocalDate birthday;

        @NotNull(message = "마케팅 동의 여부는 필수입니다.")
        private Boolean marketingAgree;

    }

    // 로그인 요청 dto
    @Getter
    @Setter
    public static class LoginRequestDTO {
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        private String email;

        @NotBlank(message = "패스워드는 필수입니다.")
        private String password;
    }
}
