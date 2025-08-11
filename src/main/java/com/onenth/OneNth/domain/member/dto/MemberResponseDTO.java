package com.onenth.OneNth.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class MemberResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberProfilePreviewDTO {
        private Long memberId;
        private String mame;
        private String profileImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupResultDTO {
        Long memberId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResultDTO {
        Long memberId;
        String accessToken;
        String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordResetResultDTO {
        Boolean isSuccess;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostListDTO {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Integer currentPage;
        Boolean isFirst;
        Boolean isLast;

        List<PostPreviewDTO> postList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewDTO {
        Long postId;
        String postType;
        String postTitle;
        String placeName;
        Double latitude;
        Double longitude;
        String regionName;
        Integer commentCount;
        Integer likeCount;
        Integer viewCount;
        String createdTime; // 예: "3시간 전", "3분 전", "2025.07.15"

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelScrapOrLikeResponseDTO {
        Boolean isSuccess;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddScrapOrLikeResponseDTO {
        Boolean isSuccess;
    }

//    @Builder
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class likedPostListDTO {
//
//    }
//
//    @Builder
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class likedPostPreviewDTO {
//
//    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class TokenReissueResultDTO {
        String accessToken;
    }

}
