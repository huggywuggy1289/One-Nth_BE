package com.onenth.OneNth.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        //String regionName;
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

    // 미리보기 하나
    @Getter
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class ItemPreviewDTO {
        private Long itemId;             // 공통 ID
        private String itemType;         // "PURCHASE" | "SHARE" (또는 "같이사요" | "함께나눠요")
        private String productName;      // Purchase: name / Sharing: title
        private Integer price;           // 현재 노출 가격 (필수)
        private Integer quantity;        // SharingItem만 값 존재 (없으면 null)
        private Integer originalPrice;   // PurchaseItem의 원래 구매가 등 (없으면 null)
        private String createdTime;      // 상대시간 문자열
        @JsonIgnore
        private LocalDateTime createdAt; // 정렬용(응답에 노출X)
        @JsonIgnore
        private LocalDateTime scrappedAt; // 스크랩 시각(정렬용)
    }

    // 목록 + 페이지 정보
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemPreviewListDTO {
        private List<ItemPreviewDTO> items;

        private Integer page;       // 1-base
        private Integer size;
        private Long totalCount;    // 대략 합계(정확 합계 원하면 둘 다 count 쿼리)
        private Boolean hasNext;

    }

}
