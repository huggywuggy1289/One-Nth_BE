package com.onenth.OneNth.domain.post.dto;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import com.onenth.OneNth.domain.region.entity.Region;
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
    private Long memberId; // 실제 toEntity에는 사용 X
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
