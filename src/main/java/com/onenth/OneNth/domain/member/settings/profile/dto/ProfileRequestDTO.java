package com.onenth.OneNth.domain.member.settings.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProfileRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class UpdateNicknameRequestDTO {
        @Schema(description = "변경할 닉네임", example = "갈래")
        @NotBlank(message = "변경할 닉네임은 필수입니다.")
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdatePasswordRequestDTO {
        @Schema(description = "변경할 비밀번호", example = "abcd1234!")
        @NotBlank(message = "변경할 비밀번호는 필수입니다.")
        private String password;
    }

}
