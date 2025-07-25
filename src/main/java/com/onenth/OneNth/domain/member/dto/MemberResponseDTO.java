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
        String regionName;
        Integer commentCount;
        Integer likeCount;
        Integer viewCount;
        String createdTime; // 예: "3시간 전", "3분 전", "2025.07.15"

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
}
