package com.onenth.OneNth.domain.post.dto;

import com.onenth.OneNth.domain.post.entity.enums.PostType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDTO {

    private PostType postType;
    private String title;
    private String content;
    private Double latitude;
    private Double longitude;
    private String link;
    private Long memberId;
    private Long regionId;

    @Builder
    public PostSaveRequestDTO(PostType postType, String title, String content,
                              Double latitude, Double longitude, String link,
                              Long memberId, Long regionId) {
        this.postType = postType;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.link = link;
        this.memberId = memberId;
        this.regionId = regionId;
    }
}
