package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.map.enums.MarkerType;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.repository.PostRepository;
import com.onenth.OneNth.domain.post.repository.scrapRepository.ScrapRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostMapService {

    private final PostRepository postRepository;
    private final RegionResolver regionResolver;
    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;

    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId, MarkerType markerType) {

        Region region = regionResolver.resolveRegion(userId, regionId);

        List<Post> posts = postRepository.findAllByRegionAndMarkerTypeWithLocation(region, markerType.getPostType());

        List<MapResponseDTO.MarkerSummary> summaries = posts.stream().map(
                post -> MapConverter.toMarkerSummary(post, markerType)
        ).collect(Collectors.toList());

        return MapConverter.toGetMarkersResponseDTO(summaries);
    }

    public MapResponseDTO.GetPostMarkerDetailsResponseDTO getMarkerDetails(Long userId, List<Long> postIds) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Post> posts = postIds.stream().map(
                postId -> postRepository.findById(postId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND_POST))
        ).collect(Collectors.toList());

        List<MapResponseDTO.PostMarkerDetail> postMarkerDetails = posts.stream().map(
                post -> MapConverter.toPostMarkerDetail(
                        post,
                        scrapRepository.existsByMemberAndPost(member, post)
                )
        ).collect(Collectors.toList());

        return MapConverter.toGetPostMarkerDetailsResponseDTO(postMarkerDetails);

    }
}
