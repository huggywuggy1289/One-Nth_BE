package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.map.enums.MarkerType;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.repository.PostRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostMapService {

    private final PostRepository postRepository;
    private final RegionResolver regionResolver;
    private final MemberRepository memberRepository;

    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId, MarkerType markerType) {

        Region region = regionResolver.resolveRegion(userId, regionId);

        List<Post> posts = postRepository.findAllByRegionAndMarkerTypeWithLocation(region, markerType.getPostType());

        Map<String, List<Post>> grouped = posts.stream()
                .collect(Collectors.groupingBy(post -> getLocationKey(post.getLatitude(), post.getLongitude())));

        List<MapResponseDTO.GroupedMarkerSummary> groupedMarkerSummaries = grouped.entrySet().stream()
                .map(entry -> {
                    List<Post> groupedPosts = entry.getValue();

                    String[] latLng = entry.getKey().split("-");

                    List<MapResponseDTO.MarkerSummary> markers = groupedPosts.stream().map(post ->
                            MapConverter.toMarkerSummary(post, markerType)).toList();

                    return MapResponseDTO.GroupedMarkerSummary.builder()
                            .latitude(Double.parseDouble(latLng[0]))
                            .longitude(Double.parseDouble(latLng[1]))
                            .markers(markers)
                            .build();
                }).toList();

        return MapConverter.toGetMarkersResponseDTO(groupedMarkerSummaries);
    }

    public MapResponseDTO.GetPostMarkerDetailsResponseDTO getMarkerDetails(Long userId, List<Long> postIds) {

        memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<MapResponseDTO.PostMarkerDetail> postMarkerDetails = postRepository.findMarkerDetailsWithScrap(postIds, userId);

        return MapConverter.toGetPostMarkerDetailsResponseDTO(postMarkerDetails);

    }

    private String getLocationKey(Double latitude, Double longitude) {
        return String.format("%.4f-%.4f", latitude, longitude);
    }
}
