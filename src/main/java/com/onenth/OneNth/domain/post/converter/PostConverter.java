package com.onenth.OneNth.domain.post.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.post.dto.PostSaveRequestDTO;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import com.onenth.OneNth.domain.region.entity.Region;

public class PostConverter {

    public static Post toEntity(PostSaveRequestDTO dto, PostType postType, Member member, Region region) {
        return Post.builder()
                .postType(postType)
                .title(dto.getTitle())
                .content(dto.getContent())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .address(dto.getAddress())
                .regionName(dto.getRegionName())
                .placeName(dto.getPlaceName())
                .link(dto.getLink())
                .member(member)
                .region(region)
                .build();
    }

}
