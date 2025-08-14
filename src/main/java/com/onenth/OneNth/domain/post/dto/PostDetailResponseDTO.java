package com.onenth.OneNth.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostDetailResponseDTO {

    private String postId;
    private String nickname;
    private String profileImageUrl;
    private String regionName;
    private String title;
    private String content;
    private List<String> imageUrls;
    private int commentCount;
    private int likeCount;
    private boolean scrapStatus;
    private int viewCount;
    private List<String> tags;
    private String address;
    private String placeName;
    private String link;
    private LocalDateTime createdAt;
}
