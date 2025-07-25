package com.onenth.OneNth.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String address;
    @Setter
    private String placeName;
    @Setter
    private String link;
    @Setter
    @Schema(hidden = true)
    private String regionName;
    @Setter
    @Schema(hidden = true)
    private Integer regionId;
    @Setter
    @Schema(hidden = true)
    private Double latitude;
    @Setter
    @Schema(hidden = true)
    private Double longitude;

    @Builder
    public PostSaveRequestDTO(String title, String content,
                              Double latitude, Double longitude, String link,
                              String regionName, Integer regionId) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.link = link;
        this.regionName = regionName;
        this.regionId = regionId;
    }
}
