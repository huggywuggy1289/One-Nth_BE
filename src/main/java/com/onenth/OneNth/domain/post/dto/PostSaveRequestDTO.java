package com.onenth.OneNth.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostSaveRequestDTO {

    private String title;
    private String content;
    @Setter
    private Double latitude;
    @Setter
    private Double longitude;
    @Setter
    private String link;
    private Long regionId;

    @Builder
    public PostSaveRequestDTO(String title, String content,
                              Double latitude, Double longitude, String link,
                              Long regionId) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.link = link;
        this.regionId = regionId;
    }
}
